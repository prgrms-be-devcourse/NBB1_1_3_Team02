package com.example.bookYourSeat.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class SwaggerConfig {
    @Bean
    open fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("book-your-seat API")
                    .description("book-your-seat의 API 문서입니다.")
                    .version("1.0.0")
            )
    }

    //UserController 설정
    @Bean
    open fun bookYourSeatApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("user API")
            .pathsToMatch("/api/v1/users/**")
            .build()
    }
}
