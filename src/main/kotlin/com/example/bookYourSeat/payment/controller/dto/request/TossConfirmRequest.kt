package com.example.bookYourSeat.payment.controller.dto.request

import com.example.bookYourSeat.payment.PaymentConst.ENTER_AMOUNT
import com.example.bookYourSeat.payment.PaymentConst.ENTER_ORDER_ID
import com.example.bookYourSeat.payment.PaymentConst.ENTER_PAYMENTKEY
import com.example.bookYourSeat.reservation.contorller.dto.PaymentRequest
import jakarta.validation.constraints.NotNull


data class TossConfirmRequest(
    val paymentKey: @NotNull(message = ENTER_PAYMENTKEY) String?,
    val orderId: @NotNull(message = ENTER_ORDER_ID) String?,
    val amount: @NotNull(message = ENTER_AMOUNT) Long?
) {
    companion object {
        fun from(request: PaymentRequest): TossConfirmRequest {
            return TossConfirmRequest(
                request.paymentKey,
                request.orderId,
                request.amount
            )
        }
    }
}
