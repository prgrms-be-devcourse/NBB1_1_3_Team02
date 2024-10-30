package com.example.bookYourSeat.payment.controller.dto.request

import com.example.bookYourSeat.payment.PaymentConst.ENTER_ORDER_ID
import jakarta.validation.constraints.NotNull


data class FinalPriceRequest(
    val seatIds: @NotNull(message = ENTER_ORDER_ID) MutableList<Long>,

    val userCouponId: Long
)
