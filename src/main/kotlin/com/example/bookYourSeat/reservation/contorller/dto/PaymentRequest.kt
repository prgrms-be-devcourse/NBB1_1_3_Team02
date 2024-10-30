package com.example.bookYourSeat.reservation.contorller.dto

import com.example.book_your_seat.payment.PaymentConst.*
import jakarta.validation.constraints.NotNull

data class PaymentRequest(
    @field:NotNull(message = ENTER_PAYMENTKEY) val paymentKey: String,
    @field:NotNull(message = ENTER_ORDER_ID) val orderId: String,
    @field:NotNull(message = ENTER_AMOUNT) val amount: Long,
    @field:NotNull(message = ENTER_SEAT_ID) val seatIds: MutableList<Long>,
    @field:NotNull(message = ENTER_ADDRESS_ID) val addressId: Long,

    val concertId: Long,
    val userCouponId: Long
)
