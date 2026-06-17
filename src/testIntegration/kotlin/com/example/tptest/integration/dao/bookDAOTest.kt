package com.example.tptest.infrastructure.driven.postgres

import com.example.tptest.domain.model.Book
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
class BookDAOIT {

    companion object {
        val postgres = PostgreSQLContainer("postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test")
            .apply { start() }   // 👈 IMPORTANT (sans @Container)

        @JvmStatic
        @DynamicPropertySource
        fun overrideDatasourceProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url") { postgres.jdbcUrl }
            registry.add("spring.datasource.username") { postgres.username }
            registry.add("spring.datasource.password") { postgres.password }
        }
    }

    @Autowired
    lateinit var bookDAO: BookDAO

    @Autowired
    lateinit var jdbcTemplate: NamedParameterJdbcTemplate

    @BeforeEach
    fun clean() {
        jdbcTemplate.update("DELETE FROM book", emptyMap<String, Any>())
    }

    @Test
    fun `should insert and read books from database`() {

        // 1. PREPARATION
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
                author = "Author B"
            )
        )

        // 3. VERIFICATION
        val result = bookDAO.findAll()

        result.size shouldBe 2
    }
}