package com.example.tptest.infrastructure.driving.controller

import com.example.tptest.domain.exception.BookAlreadyReservedException
import com.example.tptest.domain.exception.BookNotFoundException
import com.example.tptest.domain.exception.BookNotReservedException
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

fun handleException(ex: Exception, defaultMessage: String): Nothing {
    val status = when (ex) {
        is BookNotFoundException -> HttpStatus.NOT_FOUND
        is BookAlreadyReservedException,
        is BookNotReservedException -> HttpStatus.PRECONDITION_FAILED
        else -> when (val cause = ex.cause) {
            is BookNotFoundException -> HttpStatus.NOT_FOUND
            is BookAlreadyReservedException,
            is BookNotReservedException -> HttpStatus.PRECONDITION_FAILED
            else -> null
        }
    }

    throw ResponseStatusException(
        status ?: HttpStatus.INTERNAL_SERVER_ERROR,
        ex.message ?: defaultMessage,
        ex
    )
}
