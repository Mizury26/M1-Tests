package com.example.tptest.infrastructure.application

import com.example.tptest.domain.port.IBookPort
import com.example.tptest.domain.usecase.BookUseCase
import com.example.tptest.infrastructure.driven.postgres.BookDAO
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UseCasesConfiguration {

    @Bean
    fun bookUseCase(bookPort: BookDAO): BookUseCase {
        return BookUseCase(bookPort)
    }
}