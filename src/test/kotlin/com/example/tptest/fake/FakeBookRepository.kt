package com.example.tptest.fake

import com.example.tptest.domain.exception.BookNotFoundException
import com.example.tptest.domain.model.Book
import com.example.tptest.domain.port.IBookPort

class FakeBookRepository : IBookPort {
    private val books = mutableListOf<Book>()

    override fun save(book: Book): Book {
        books.add(book)
        return book
    }

    override fun findAll(): List<Book> {
        return books.toList()
    }

    override fun findByd(id: String): Book {
        return books.firstOrNull { it.id == id }
            ?: throw BookNotFoundException("Livre non trouvé pour l'id $id")
    }

    override fun update(book: Book) {
        val index = books.indexOfFirst { it.id == book.id }
        if (index == -1) {
            throw BookNotFoundException("Livre non trouvé pour l'id ${book.id}")
        }
        books[index] = book
    }
}
