package com.example.tptest.domain.usecase

import com.example.tptest.domain.model.Book
import com.example.tptest.domain.port.IBookRepository

class BookUseCase {
    private val bookRepository: IBookRepository

    constructor(bookRepository: IBookRepository) {
        this.bookRepository = bookRepository
    }

    fun addBook(book: Book) {
        bookRepository.save(book)
    }

    fun getAllBooks(): List<Book> {
        return bookRepository.findAll().sortedBy(Book::title)
    }
}