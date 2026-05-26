package com.example.tptest

fun cypher(letter: Char, key: Int): Char {
    if (!letter.isUpperCase()) {
        throw IllegalArgumentException("Input must be an uppercase char letter")
    }
    if (key < 0) {
        throw IllegalArgumentException("Key must be a non-negative integer")
    }
    val alphabet = ('A'..'Z').toList()
    val index = alphabet.indexOf(letter)
    val newIndex = (index + key) % 26
    return alphabet[newIndex]
}