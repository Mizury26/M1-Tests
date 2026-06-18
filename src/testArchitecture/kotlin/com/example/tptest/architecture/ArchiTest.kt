package com.example.tptest.architecture

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.library.Architectures.layeredArchitecture
import io.kotest.core.spec.style.FunSpec

private const val BASE_PACKAGE = "com.example.tptest"

class ArchiTest : FunSpec() {

    private val importedClasses: JavaClasses = ClassFileImporter()
        .withImportOption(ImportOption.DoNotIncludeTests())
        .importPackages(BASE_PACKAGE)

    init {
        test("le domaine ne doit pas dépendre de l'infrastructure") {
            val rule = layeredArchitecture().consideringAllDependencies()
                .layer("Domain Model").definedBy("$BASE_PACKAGE.domain.model..", "$BASE_PACKAGE.domain.exception..")
                .layer("Domain Port").definedBy("$BASE_PACKAGE.domain.port..")
                .layer("Domain UseCase").definedBy("$BASE_PACKAGE.domain.usecase..")
                .layer("Domain Exception").definedBy("$BASE_PACKAGE.domain.exception..")
                .layer("Infrastructure").definedBy("$BASE_PACKAGE.infrastructure..")
                .optionalLayer("Standard API").definedBy(
                    "java..", "kotlin..", "kotlinx..",
                    "org.jetbrains.annotations.."
                )
//                .withOptionalLayers(true)
                .whereLayer("Domain Model").mayOnlyAccessLayers("Standard API")
                .whereLayer("Domain Port").mayOnlyAccessLayers("Standard API", "Domain Model")
                .whereLayer("Domain UseCase").mayOnlyAccessLayers("Standard API", "Domain Model", "Domain Port", "Domain Exception")

            rule.check(importedClasses)
        }

        test("les parties de l'infrastructure ne doivent pas s'appeler entre elles") {
            val rule = layeredArchitecture().consideringAllDependencies()
                .layer("Driving").definedBy("$BASE_PACKAGE.infrastructure.driving..")
                .layer("Driven").definedBy("$BASE_PACKAGE.infrastructure.driven..")
                .layer("Application").definedBy("$BASE_PACKAGE.infrastructure.application..")
                .layer("Domain").definedBy("$BASE_PACKAGE.domain..")
                .optionalLayer("Standard API").definedBy(
                    "java..", "kotlin..", "kotlinx..",
                    "org.jetbrains.annotations..",
                    "org.springframework..",
                    "jakarta.."
                )
//                .withOptionalLayers(true)
                // Driving controllers may call application/use-cases, domain models and standard APIs
                .whereLayer("Driving").mayOnlyAccessLayers("Standard API", "Application", "Domain")
                // Driven implementations (adapters) may call domain and standard APIs
                .whereLayer("Driven").mayOnlyAccessLayers("Standard API", "Domain")
                // Application layer may call domain, driven (wiring) and standard APIs
                .whereLayer("Application").mayOnlyAccessLayers("Standard API", "Domain", "Driven")

            rule.check(importedClasses)
        }
    }
}
