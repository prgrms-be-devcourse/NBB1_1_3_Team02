package com.example.bookYourSeat.payment.controller

import com.example.bookYourSeat.payment.controller.dto.request.TossConfirmRequest
import com.example.bookYourSeat.payment.controller.dto.response.TossConfirmResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(name = "TossOpenFeign", url = "https://api.tosspayments.com")
interface TossOpenFeign {
    @PostMapping("/payments/confirm")
    fun confirm(
        @RequestHeader(HttpHeaders.AUTHORIZATION) authorization: String,
        @RequestBody request: TossConfirmRequest
    ): TossConfirmResponse
}
