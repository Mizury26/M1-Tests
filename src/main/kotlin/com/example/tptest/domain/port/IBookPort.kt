package com.example.tptest.domain.port
import com.example.tptest.domain.model.Book

interface IBookPort {
    fun save(book: Book): Book
    fun findAll(): List<Book>
    fun findByd(id: String): Book
    fun update(book: Book)
}
