package com.example.tptest.infrastructure.driven

import com.example.tptest.domain.model.Book
import com.example.tptest.domain.port.IBookPort
import org.springframework.stereotype.Service

@Service
class FakeBook: IBookPort {
    override fun save(book: Book) {
        TODO("Not yet implemented")
    }

    override fun findAll(): List<Book> {
        TODO("Not yet implemented")
    }
}