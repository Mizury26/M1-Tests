package com.example.tptest

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.element
import io.kotest.property.forAll

@SpringBootTest
class CypherUnitTests : FunSpec({
	test("Cypher algo char 'A' and 2 should equal 'C'") {
		val a = 'A'
		val b = 2

		val res = cypher(a, b)

		res shouldBe 'C'
	}

	test("Cypher algo char 'A' and 27 should equal 'B'") {
		val a = 'A'
		val b = 27

		val res = cypher(a, b)

		res shouldBe 'B'
	}

	test("Cypher with lowercase letter should throw IllegalArgumentException") {
		val exception = shouldThrow<IllegalArgumentException> {
			cypher('a', 2)
		}

		exception.message shouldBe "Input must be an uppercase char letter"
	}

	test("Cypher with negative key should throw IllegalArgumentException") {
		val exception = shouldThrow<IllegalArgumentException> {
			cypher('A', -1)
		}

		exception.message shouldBe "Key must be a non-negative integer"
	}
})

class CypherInvariantsTests : FunSpec({
	// Générateur de lettres majuscules
	val upperChar = Arb.element(('A'..'Z').toList())
	val lowerChar = Arb.element(('a'..'z').toList())

	test("cypher return always an upper character") {
		forAll(upperChar, Arb.int(0, 1000)) { letter, key ->
			val result = cypher(letter, key)
			result in 'A'..'Z'
		}
	}

	test("cypher with lowercase letter should throw IllegalArgumentException") {
		forAll(lowerChar, Arb.int(0, 1000)) { letter, key ->
			val result = shouldThrow<IllegalArgumentException> {
				cypher(letter, key)
			}
			result.message shouldBe "Input must be an uppercase char letter"
			true
		}
	}

	test("cypher with key = 26 must return the same lettre (invariant of cycle)") {
		forAll(upperChar) { letter ->
			cypher(letter, 26) == letter
		}
	}

	test("cypher with key = 0 must return the same lettre (invariant of identity)") {
		forAll(upperChar) { letter ->
			cypher(letter, 0) == letter
		}
	}

	test("cypher avec key = 52 retourne la même lettre (2 × 26)") {
		forAll(upperChar) { letter ->
			cypher(letter, 52) == letter
		}
	}

	test (name = "cypher with negative key should throw IllegalArgumentException") {
		forAll(upperChar, Arb.int(-1000, -1)) { letter, key ->
			val result = shouldThrow<IllegalArgumentException> {
				cypher(letter, key)
			}
			result.message shouldBe "Key must be a non-negative integer"
			true
		}
	}

})