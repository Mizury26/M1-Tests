package com.example.tptest

private const val ALPHABET_SIZE = 26

fun cypher(letter: Char, key: Int): Char {
    require(letter.isUpperCase()) { "Input must be an uppercase char letter" }
    require(key >= 0) { "Key must be a non-negative integer" }

    val alphabet = ('A'..'Z').toList()
    val index = alphabet.indexOf(letter)
    val newIndex = (index + key) % ALPHABET_SIZE
    return alphabet[newIndex]
}
