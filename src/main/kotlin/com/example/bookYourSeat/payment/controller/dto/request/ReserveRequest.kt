package com.example.bookYourSeat.payment.controller.dto.request

data class ReserveRequest(
    val seatsId: List<Long>,
    val userCouponId: Long,
    val addressId: Long
)