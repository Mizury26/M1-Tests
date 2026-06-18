package com.example.tptest.infrastructure.driving.controller

import com.example.tptest.domain.model.Book
import com.example.tptest.domain.usecase.BookUseCase
import com.example.tptest.infrastructure.driving.dto.BookDTO
import com.example.tptest.infrastructure.driving.dto.BookDTOCreate
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/books")
class BookController(
    private val bookUseCase: BookUseCase
) {

    @GetMapping
    fun getBook(): List<BookDTO> =
        bookUseCase.getAllBooks().map { book -> mappingBookToBookDTO(book) }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addBook(@Valid @RequestBody book: BookDTOCreate) {
        bookUseCase.addBook(mappingBookDTOToBook(book))
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleInvalidJson(): ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Corps de la requête invalide")

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(e: RuntimeException): ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.message)
}

fun mappingBookToBookDTO(book: Book): BookDTO =
    BookDTO(requireNotNull(book.id), book.title, book.author)

fun mappingBookDTOToBook(bookDTO: BookDTOCreate): Book =
    Book(
        id = null,
        title = bookDTO.title,
        author = bookDTO.author
    )
