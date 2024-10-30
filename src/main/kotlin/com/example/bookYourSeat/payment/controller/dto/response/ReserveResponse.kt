package com.example.bookYourSeat.payment.controller.dto.response

import java.util.*


data class ReserveResponse(
    val paymentId: UUID,
    val concludePrice: Long
)
