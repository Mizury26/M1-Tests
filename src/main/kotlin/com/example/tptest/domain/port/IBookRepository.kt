package com.example.tptest.domain.port
import com.example.tptest.domain.model.Book

interface IBookRepository {
    fun save(book: Book)
    fun findAll(): List<Book>
}