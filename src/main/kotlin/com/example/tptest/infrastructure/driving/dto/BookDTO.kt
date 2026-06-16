package com.example.tptest.infrastructure.driving.dto;
import jakarta.validation.constraints.NotBlank

data class BookDTO (
    @field:NotBlank
    val title: String,
    @field:NotBlank
    val author: String,
)
