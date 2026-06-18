package com.example.tptest.domain.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
class BookAlreadyReservedException(message: String? = null) : RuntimeException(message)
