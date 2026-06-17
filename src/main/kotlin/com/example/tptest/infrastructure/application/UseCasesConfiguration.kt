package com.example.tptest.infrastructure.application

import com.example.tptest.domain.port.IBookPort
import com.example.tptest.domain.usecase.BookUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UseCasesConfiguration {

    @Bean
    fun bookUseCase(bookPort: IBookPort): BookUseCase {
        return BookUseCase(bookPort)
    }
}