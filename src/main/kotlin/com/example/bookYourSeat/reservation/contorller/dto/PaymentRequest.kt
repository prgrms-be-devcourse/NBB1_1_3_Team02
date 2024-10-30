package com.example.bookYourSeat.reservation.contorller.dto

import com.example.bookYourSeat.payment.PaymentConst.ENTER_ADDRESS_ID
import com.example.bookYourSeat.payment.PaymentConst.ENTER_AMOUNT
import com.example.bookYourSeat.payment.PaymentConst.ENTER_ORDER_ID
import com.example.bookYourSeat.payment.PaymentConst.ENTER_PAYMENTKEY
import com.example.bookYourSeat.payment.PaymentConst.ENTER_SEAT_ID
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
