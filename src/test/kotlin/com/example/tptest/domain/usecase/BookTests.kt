package com.example.tptest

import com.example.tptest.domain.model.Book
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.assertions.throwables.shouldThrow
import com.example.tptest.domain.fake.FakeBookRepository
import com.example.tptest.domain.port.IBookPort
import com.example.tptest.domain.usecase.BookUseCase
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldContainAll
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

@SpringBootTest
class BookUnitTests : FunSpec({
    test("A book should have a non-empty title") {
        shouldThrow<IllegalArgumentException> {
            Book(id = null, title = "", author = "Victor Hugo")
        }
    }

    test("A book should have a non-empty author") {
        shouldThrow<IllegalArgumentException> {
            Book(id = null, title = "Les Misérables", author = "")
        }
    }
})

class BookInvariantsTests : FunSpec({
    test("The list of book must return all of the books") {
        val repository = FakeBookRepository()
        val useCase = BookUseCase(repository)

        val book1 = Book(id = null, title = "Le Petit Prince", author = "Antoine de Saint-Exupéry")
        val book2 = Book(id = null, title = "Les Misérables", author = "Victor Hugo")

        useCase.addBook(book1)
        useCase.addBook(book2)

        val result = useCase.getAllBooks()

        result shouldHaveSize 2
        result shouldContainAll listOf(book1, book2)
    }

    test("The list of book must return all of the books sorted by title") {
        val repository = mockk<IBookPort>()
        val useCase = BookUseCase(repository)

        val books = listOf(
            Book(id = null, title = "Zola", author = "Émile Zola"),
            Book(id = null, title = "A l'ombre", author = "Auteur"),
            Book(id = null, title = "Moby Dick", author = "Herman Melville")
        )

        every { repository.findAll() } returns books

        val result = useCase.getAllBooks()

        result.map { it.title } shouldBe listOf("A l'ombre", "Moby Dick", "Zola")
        verify(exactly = 1) { repository.findAll() }
    }

})