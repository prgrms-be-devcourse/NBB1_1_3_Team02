package com.example.bookYourSeat.payment.controller.dto.request


data class ConfirmRequest(
    val paymentKey: String,
    val orderId: String,
    val amount: Long,
    val seatsId: List<Long>,
    val userCouponId: Long
)
