package com.example.tptest.domain.usecase

import com.example.tptest.domain.model.Book
import org.springframework.boot.test.context.SpringBootTest
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.assertions.throwables.shouldThrow
import com.example.tptest.domain.exception.BookAlreadyReservedException
import com.example.tptest.domain.exception.BookNotReservedException
import com.example.tptest.domain.port.IBookPort
import com.example.tptest.fake.FakeBookRepository
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldContainAll
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

@SpringBootTest
class BookUnitTests : FunSpec({
    test("A book should have a non-empty title") {
        shouldThrow<IllegalArgumentException> {
            Book(id = null, title = "", author = "Victor Hugo", is_reserved = false)
        }
    }

    test("A book should have a non-empty author") {
        shouldThrow<IllegalArgumentException> {
            Book(id = null, title = "Les Misérables", author = "", is_reserved = false)
        }
    }
})

class BookInvariantsTests : FunSpec({
    test("The list of book must return all of the books") {
        val repository = FakeBookRepository()
        val useCase = BookUseCase(repository)

        val book1 = Book(id = null, title = "Le Petit Prince", author = "Antoine de Saint-Exupéry", is_reserved = false)
        val book2 = Book(id = null, title = "Les Misérables", author = "Victor Hugo", is_reserved = true)

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
            Book(id = null, title = "Zola", author = "Émile Zola", is_reserved = true),
            Book(id = null, title = "A l'ombre", author = "Auteur", is_reserved = false),
            Book(id = null, title = "Moby Dick", author = "Herman Melville", is_reserved = false)
        )

        every { repository.findAll() } returns books

        val result = useCase.getAllBooks()

        result.map { it.title } shouldBe listOf("A l'ombre", "Moby Dick", "Zola")
        verify(exactly = 1) { repository.findAll() }
    }

    test("Reserving an already reserved book should throw BookAlreadyReservedException") {
        val repository = FakeBookRepository()
        val book = Book(id = "1", title = "Les Misérables", author = "Victor Hugo", is_reserved = true)
        repository.save(book)

        val useCase = BookUseCase(repository)

        shouldThrow<BookAlreadyReservedException> {
            useCase.reserveBook("1")
        }
    }

    test("Dereserving a not reserved book should throw BookNotReservedException") {
        val repository = FakeBookRepository()
        val book = Book(id = "1", title = "Le Petit Prince", author = "Antoine de Saint-Exupéry", is_reserved = false)
        repository.save(book)

        val useCase = BookUseCase(repository)

        shouldThrow<BookNotReservedException> {
            useCase.dereserveBook("1")
        }
    }

})
