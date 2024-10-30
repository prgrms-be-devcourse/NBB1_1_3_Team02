package com.example.bookYourSeat.payment.controller.dto.response

import java.time.LocalDateTime

data class TossConfirmResponse(
    val orderId: String,
    val totalAmount: Long,
    val paymentKey: String,
    val approvedAt: LocalDateTime
)
