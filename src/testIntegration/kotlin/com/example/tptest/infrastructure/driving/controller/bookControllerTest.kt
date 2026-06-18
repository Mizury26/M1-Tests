package com.example.tptest.infrastructure.driving.controller

import com.example.tptest.domain.model.Book
import com.example.tptest.domain.usecase.BookUseCase
import com.example.tptest.infrastructure.driving.controller.BookController
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.FunSpec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import io.mockk.every
import io.mockk.just
import io.mockk.Runs
import io.mockk.verify
import io.kotest.extensions.spring.SpringExtension

@WebMvcTest(BookController::class)
class BookControllerTest : FunSpec() {


    @Autowired
    lateinit var mockMvc: MockMvc

    @MockkBean
    lateinit var bookUseCase: BookUseCase

    init {
        extensions(SpringExtension)

        test("GET books should return books") {

            every {
                bookUseCase.getAllBooks()
            } returns listOf(
                Book("", "Clean Code", "Robert Martin"),
                Book("", "DDD", "Eric Evans")
            )

            mockMvc.get("/books")
                .andExpect {
                    status { isOk() }
                    jsonPath("$[0].title") { value("Clean Code") }
                    jsonPath("$[0].author") { value("Robert Martin") }
                }

            verify(exactly = 1) {
                bookUseCase.getAllBooks()
            }
        }

        test("POST books should call use case") {

            every {
                bookUseCase.addBook(any())
            } just Runs

            mockMvc.post("/books") {
                contentType = MediaType.APPLICATION_JSON
                content = """
                    {
                        "title":"Clean Code",
                        "author":"Robert Martin"
                    }
                """.trimIndent()
            }.andExpect {
                status { isCreated() }
            }

            verify(exactly = 1) {
                bookUseCase.addBook(any())
            }
        }

        test("should return 400 when body is invalid") {

            mockMvc.post("/books") {
                contentType = MediaType.APPLICATION_JSON
                content = """{ "title": "" }"""
            }.andExpect {
                status { isBadRequest() }
            }
        }

        test("should return 500 when use case throws exception") {

            every {
                bookUseCase.getAllBooks()
            } throws RuntimeException("DB error")

            mockMvc.get("/books")
                .andExpect {
                    status { isInternalServerError() }
                }
        }
    }
}