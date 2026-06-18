package com.example.tptest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TpTestApplication

@Suppress("SpreadOperator")
fun main(args: Array<String>) {
	runApplication<TpTestApplication>(*args)
}

