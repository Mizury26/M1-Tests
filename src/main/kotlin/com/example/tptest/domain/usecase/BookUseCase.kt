package com.example.tptest.domain.usecase

import com.example.tptest.domain.model.Book
import com.example.tptest.domain.port.IBookPort

class BookUseCase {
    private val bookPort: IBookPort

    constructor(bookPort: IBookPort) {
        this.bookPort = bookPort
    }

    fun addBook(book: Book) {
        bookPort.save(book)
    }

    fun getAllBooks(): List<Book> {
        return bookPort.findAll().sortedBy(Book::title)
    }
}