package com.example.tptest.domain.usecase

import com.example.tptest.domain.exception.BookAlreadyReservedException
import com.example.tptest.domain.exception.BookNotFoundException
import com.example.tptest.domain.exception.BookNotReservedException
import com.example.tptest.domain.model.Book
import com.example.tptest.domain.port.IBookPort

class BookUseCase {
    private val bookPort: IBookPort

    constructor(bookPort: IBookPort) {
        this.bookPort = bookPort
    }

    fun addBook(book: Book): Book {
        return bookPort.save(book)
    }

    fun getAllBooks(): List<Book> {
        return bookPort.findAll().sortedBy(Book::title)
    }

    fun reserveBook(bookId: String) {
        val initBook = bookPort.findByd(bookId)
        if (initBook == null) {
            throw BookNotFoundException("Livre non trouvé pour l'id $bookId")
        }
        if (initBook.isReserved) {
            throw BookAlreadyReservedException("Livre déjà réservé")
        }
        val reservedBook = initBook.copy(isReserved = true)
        bookPort.update(reservedBook)
    }

    fun dereserveBook(bookId: String) {
        val initBook = bookPort.findByd(bookId)
        if (initBook == null) {
            throw BookNotFoundException("Livre non trouvé pour l'id $bookId")
        }
        if (!initBook.isReserved) {
            throw BookNotReservedException("Livre n'est pas réservé")
        }
        val reservedBook = initBook.copy(isReserved = false)
        bookPort.update(reservedBook)
    }
}
