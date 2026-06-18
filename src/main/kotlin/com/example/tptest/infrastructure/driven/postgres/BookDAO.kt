package com.example.tptest.infrastructure.driven.postgres
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import com.example.tptest.domain.model.Book
import com.example.tptest.domain.port.IBookPort
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service
import kotlin.collections.mapOf

@Service
class BookDAO(private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : IBookPort {
    override fun findAll(): List<Book> {
        return namedParameterJdbcTemplate
            .query("SELECT * FROM book", MapSqlParameterSource()) { rs, _ ->
                Book(
                    id = rs.getString("id"),
                    title = rs.getString("title"),
                    author = rs.getString("author"),
                )
            }
    }
    override fun save(book: Book) {
        namedParameterJdbcTemplate.update(
            """
        INSERT INTO book(title, author)
        VALUES (:title, :author)
        """.trimIndent(),
            mapOf(
                "title" to book.title,
                "author" to book.author
            )
        )
    }
}
