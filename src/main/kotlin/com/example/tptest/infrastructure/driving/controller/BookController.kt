package com.example.tptest.infrastructure.driving.controller;

import com.example.tptest.infrastructure.driving.dto.BookDTO
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.http.HttpStatus
import com.example.tptest.domain.usecase.BookUseCase
import com.example.tptest.domain.model.Book


@RestController
@RequestMapping("/books")
class BookController(
    private val bookUseCase: BookUseCase
) {

    // GET localhost:8080/books returns [{"title":"titre1","author": "author1"},{"titel":"titre2","author": "author2"}]
    @GetMapping
    fun getBook(): List<BookDTO> {
        val books = bookUseCase.getAllBooks()
        return books.map { book -> mappingBookToBookDTO(book) }
    }

    // POST localhost:8080/books with body {"title":"title_test","author":"author_test"} returns 201 Created with no content
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addBook(@RequestBody book: BookDTO) {
        bookUseCase.addBook(mappingBookDTOToBook(book))
    }
}

fun mappingBookDTOToBook(bookDTO: BookDTO): Book {
    return Book(bookDTO.title, bookDTO.author)
}

fun mappingBookToBookDTO(book: Book): BookDTO {
    return BookDTO(book.title, book.author)
}
