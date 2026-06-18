package com.example.tptest.infrastructure.driving.dto
import jakarta.validation.constraints.NotBlank

data class BookDTOCreate (
    @field:NotBlank
    val title: String,
    @field:NotBlank
    val author: String,
)
