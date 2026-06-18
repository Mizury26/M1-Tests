package com.example.tptest.infrastructure.driving.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.server.ResponseStatusException

fun handleException(ex: Exception, defaultMessage: String): Nothing {
    val responseStatus = ex.javaClass.getAnnotation(ResponseStatus::class.java)
        ?: ex.cause?.javaClass?.getAnnotation(ResponseStatus::class.java)

    if (responseStatus != null) {
        throw ex
    }

    throw ResponseStatusException(
        HttpStatus.INTERNAL_SERVER_ERROR,
        defaultMessage,
        ex
    )
}
