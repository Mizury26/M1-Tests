package com.example.tptest

import com.example.tptest.domain.model.Book
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.assertions.throwables.shouldThrow
import com.example.tptest.domain.fake.FakeBookRepository
import com.example.tptest.domain.port.IBookRepository
import com.example.tptest.domain.usecase.BookUseCase
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldContainAll
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

@SpringBootTest
class BookApplicationTests {
    @Test
    fun contextLoads() {}
}

class BookUnitTests : FunSpec({
    test("A book should have a non-empty title") {
        shouldThrow<IllegalArgumentException> {
            Book("", "Victor Hugo")
        }
    }

    test("A book should have a non-empty author") {
        shouldThrow<IllegalArgumentException> {
            Book("Les Misérables", "")
        }
    }
})

class BookInvariantsTests : FunSpec({
    test("The list of book must return all of the books") {
        val repository = FakeBookRepository()
        val useCase = BookUseCase(repository)

        val book1 = Book("Le Petit Prince", "Antoine de Saint-Exupéry")
        val book2 = Book("Les Misérables", "Victor Hugo")

        useCase.addBook(book1)
        useCase.addBook(book2)

        val result = useCase.getAllBooks()

        result shouldHaveSize 2
        result shouldContainAll listOf(book1, book2)
    }

    test("The list of book must return all of the books sorted by title") {
        val repository = mockk<IBookRepository>()
        val useCase = BookUseCase(repository)

        val books = listOf(
            Book("Zola", "Émile Zola"),
            Book("A l'ombre", "Auteur"),
            Book("Moby Dick", "Herman Melville")
        )

        every { repository.findAll() } returns books

        val result = useCase.getAllBooks()

        result.map { it.title } shouldBe listOf("A l'ombre", "Moby Dick", "Zola")
        verify(exactly = 1) { repository.findAll() }
    }

})