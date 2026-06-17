package com.example.tptest.component

import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.cucumber.spring.ScenarioScope
import io.restassured.RestAssured
import io.restassured.response.Response
import org.springframework.boot.test.web.server.LocalServerPort
import kotlin.test.assertEquals

@ScenarioScope
class BookStepDefs {

    @LocalServerPort
    var port = 0

    lateinit var response: Response

    @When("je récupère la liste des livres")
    fun getBooks() {

        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port

        response =
            RestAssured
                .given()
                .`when`()
                .get("/books")
    }

    @Then("le code retour est {int}")
    fun checkStatus(code: Int) {
        assertEquals(code, response.statusCode)
    }


    @When("j'ajoute un livre avec le titre {string} et l'auteur {string}")
    fun addBook(title: String, author: String) {

        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port

        response =
            RestAssured
                .given()
                .contentType("application/json")
                .body(
                    """
                    {
                        "title":"$title",
                        "author":"$author"
                    }
                    """
                )
                .post("/books")
    }
}