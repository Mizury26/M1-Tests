package com.example.tptest.domain.fake

import com.example.tptest.domain.model.Book
import com.example.tptest.domain.port.IBookRepository

class FakeBookRepository : IBookRepository {
    private val books = mutableListOf<Book>()

    override fun save(book: Book) {
        books.add(book)
    }

    override fun findAll(): List<Book> {
        return books.toList()
    }
}