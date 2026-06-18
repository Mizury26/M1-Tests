package com.example.tptest.component

import io.cucumber.java.Before
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
    private var port: Int = 0


    @Before
    fun setup() {
        RestAssured.baseURI = "http://localhost:$port"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    lateinit var response: Response
    var lastCreatedBookId: String? = null

    @When("je récupère la liste des livres")
    fun getBooks() {
        response = RestAssured
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
        response = RestAssured
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

        // Extraction de l'ID directement depuis la réponse du POST 201
        if (response.statusCode == 201) {
            lastCreatedBookId = response.jsonPath().getString("id")
        }
    }

    @When("je réserve ce livre via son ID")
    fun reserveBookWithId() {
        checkNotNull(lastCreatedBookId) { "Impossible de réserver : aucun ID de livre stocké !" }

        // Appel de ton endpoint réel : POST /books/reserve?bookId=xxx
        response = RestAssured
            .given()
            .queryParam("bookId", lastCreatedBookId)
            .`when`()
            .post("/books/reserve")
    }
}
