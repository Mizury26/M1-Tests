package com.example.tptest.infrastructure.driving.controller

import com.example.tptest.domain.model.Book
import com.example.tptest.domain.usecase.BookUseCase
import com.example.tptest.infrastructure.driving.dto.BookDTO
import com.example.tptest.infrastructure.driving.dto.BookDTOCreate
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/books")
class BookController(
    private val bookUseCase: BookUseCase
) {

    @GetMapping
    fun getBook(): List<BookDTO> =
        try {
            bookUseCase.getAllBooks().map { book -> mappingBookToBookDTO(book) }

        } catch (e: Exception) {
            handleException(e, "Erreur lors de la récupération des livres")
        }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addBook(@Valid @RequestBody book: BookDTOCreate): BookDTO {
        try {
            val created = bookUseCase.addBook(mappingBookDTOCreateToBook(book))
            return mappingBookToBookDTO(created)
        } catch (e: Exception) {
            handleException(e, "Erreur lors de l'ajout du livre")
        }
        throw IllegalStateException("Unreachable")
    }

    @PostMapping("/reserve")
    @ResponseStatus(HttpStatus.OK)
    fun reserveBook(@RequestParam bookId: String) {
        try {
            bookUseCase.reserveBook(bookId)
        } catch (e: Exception) {
            handleException(e, "Erreur lors de la réservation du livre")
        }
    }

    @PostMapping("/dereserve")
    @ResponseStatus(HttpStatus.OK)
    fun dereserveBook(@RequestParam bookId: String) {
        try {
            bookUseCase.dereserveBook(bookId)
        } catch (e: Exception) {
            handleException(e, "Erreur lors de la déréservation du livre")
        }
    }

    fun mappingBookToBookDTO(book: Book): BookDTO =
        BookDTO(requireNotNull(book.id), book.title, book.author, book.is_reserved)

    fun mappingBookDTOCreateToBook(bookDTO: BookDTOCreate): Book =
        Book(
            id = null,
            title = bookDTO.title,
            author = bookDTO.author,
            is_reserved = false
        )
}
