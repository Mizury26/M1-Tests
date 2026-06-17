package com.example.tptest.domain.model

data class Book(
    val id: String?,
    val title: String,
    val author: String
) {
    init {
        require(title.isNotBlank()) { "Le titre ne doit pas être vide" }
        require(author.isNotBlank()) { "L'auteur ne doit pas être vide" }
    }
}