package com.example.tptest.infrastructure.driving.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.server.ResponseStatusException

fun handleException(ex: Exception, defaultMessage: String): Nothing {
    // ÉTAPE 1 : Si l'exception vient du domaine et possède son propre @ResponseStatus (comme tes 412),
    // on l'extrait de son enveloppe et on la laisse remonter brute pour Spring.
    val responseStatus = ex.javaClass.getAnnotation(ResponseStatus::class.java)
        ?: ex.cause?.javaClass?.getAnnotation(ResponseStatus::class.java)

    if (responseStatus != null) {
        throw ex
    }

    // ÉTAPE 2 : Pour tout le reste (erreurs techniques, DB coupée, bugs), on force la 500
    throw ResponseStatusException(
        HttpStatus.INTERNAL_SERVER_ERROR,
        defaultMessage,
        ex
    )
}
