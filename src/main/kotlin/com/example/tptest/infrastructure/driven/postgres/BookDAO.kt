package com.example.tptest.infrastructure.driven.postgres

import com.example.tptest.domain.exception.BookNotFoundException
import com.example.tptest.domain.model.Book
import com.example.tptest.domain.port.IBookPort
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
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
                    is_reserved = rs.getBoolean("is_reserved")
                )
            }
    }

    override fun findByd(id: String): Book {
        return namedParameterJdbcTemplate.query(
            "SELECT * FROM book WHERE id = :id::uuid",
            MapSqlParameterSource().addValue("id", id)
        ) { rs, _ ->
            Book(
                id = rs.getString("id"),
                title = rs.getString("title"),
                author = rs.getString("author"),
                is_reserved = rs.getBoolean("is_reserved")
            )
        }.firstOrNull() ?: throw BookNotFoundException("Livre non trouvé pour l'id $id")
    }

    override fun update(book: Book) {
        val updatedRows = namedParameterJdbcTemplate.update(
            """
            UPDATE book
            SET title = :title,
                author = :author,
                is_reserved = :is_reserved
            WHERE id = :id::uuid
            """.trimIndent(),
            mapOf(
                "id" to requireNotNull(book.id),
                "title" to book.title,
                "author" to book.author,
                "is_reserved" to book.is_reserved
            )
        )

        if (updatedRows == 0) {
            throw BookNotFoundException("Livre non trouvé pour l'id ${book.id}")
        }
    }

    override fun save(book: Book): Book {
        return namedParameterJdbcTemplate.query(
            """
        INSERT INTO book(title, author, is_reserved)
        VALUES (:title, :author, :is_reserved)
        RETURNING id, title, author, is_reserved
        """.trimIndent(),
            MapSqlParameterSource()
                .addValue("title", book.title)
                .addValue("author", book.author)
                .addValue("is_reserved", book.is_reserved)
        ) { rs, _ ->
            Book(
                id = rs.getString("id"),
                title = rs.getString("title"),
                author = rs.getString("author"),
                is_reserved = rs.getBoolean("is_reserved")
            )
        }.first()
    }
}
