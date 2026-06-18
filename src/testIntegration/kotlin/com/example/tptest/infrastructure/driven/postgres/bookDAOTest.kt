package com.example.tptest.infrastructure.driven.postgres

import com.example.tptest.domain.model.Book
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource

import org.testcontainers.containers.PostgreSQLContainer

@SpringBootTest
class BookDAOIT : FunSpec() {
    @Autowired
    lateinit var bookDAO: BookDAO

    @Autowired
    lateinit var jdbcTemplate: NamedParameterJdbcTemplate

    companion object {
        val postgres = PostgreSQLContainer("postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test")
            .apply { start() }

        @JvmStatic
        @DynamicPropertySource
        fun overrideDatasourceProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url") { postgres.jdbcUrl }
            registry.add("spring.datasource.username") { postgres.username }
            registry.add("spring.datasource.password") { postgres.password }
        }
    }

    init {

        extensions(SpringExtension)


        beforeEach {
            clean()
        }

        test("should insert and read books from database"){
            jdbcTemplate.update(
                """
            INSERT INTO book(id, title, author)
            VALUES ('11111111-1111-1111-1111-111111111111', 'Old Book', 'Author A')
            """.trimIndent(),
                MapSqlParameterSource()
            )

            // 2. ACTION
            bookDAO.save(
                Book(
                    id = null,
                    title = "New Book",
                    author = "Author B",
                    isReserved = false
                )
            )

            // 3. VERIFICATION
            val result = bookDAO.findAll()

            result.size shouldBe 2
        }

        test("should reserve a book via update") {
            val id = "22222222-2222-2222-2222-222222222222"

            jdbcTemplate.update(
                """
        INSERT INTO book(id, title, author, is_reserved)
        VALUES (:id::uuid, 'Reservable Book', 'Author R', false)
        """.trimIndent(),
                MapSqlParameterSource().addValue("id", id) // Injection propre ici
            )

            val book = bookDAO.findByd(id)
            book.isReserved shouldBe false

            // Reserve
            bookDAO.update(book.copy(isReserved = true))

            val updated = bookDAO.findByd(id)
            updated.isReserved shouldBe true
        }

        test("should dereserve a book via update") {
            val id = "33333333-3333-3333-3333-333333333333"

            jdbcTemplate.update(
                """
        INSERT INTO book(id, title, author, is_reserved)
        VALUES (:id::uuid, 'Already Reserved', 'Author D', true)
        """.trimIndent(),
                MapSqlParameterSource().addValue("id", id)
            )

            val book = bookDAO.findByd(id)
            book.isReserved shouldBe true

            // Dereserve
            bookDAO.update(book.copy(isReserved = false))

            val updated = bookDAO.findByd(id)
            updated.isReserved shouldBe false
        }
    }

    fun clean() {
        jdbcTemplate.update("DELETE FROM book", emptyMap<String, Any>())
    }

}
