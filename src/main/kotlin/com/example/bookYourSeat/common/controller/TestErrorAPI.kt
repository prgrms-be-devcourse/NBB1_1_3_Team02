package com.example.bookYourSeat.common.controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/test")
class TestErrorAPI {

    @GetMapping
    fun testError() {
        throw IllegalArgumentException("에러 테스트")
    }
}
