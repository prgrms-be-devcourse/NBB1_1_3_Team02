package com.example.bookYourSeat.payment.service.dto

import com.example.bookYourSeat.payment.PaymentConst.INVALID_AMOUNT
import com.example.bookYourSeat.payment.controller.dto.response.TossConfirmResponse
import java.time.LocalDateTime

class PaymentCommand private constructor(
    request: PaymentRequest,
    confirmResponse: TossConfirmResponse
) {
    val orderId: String = confirmResponse.orderId
    val totalAmount: Long = confirmResponse.totalAmount
    val paymentKey: String = confirmResponse.paymentKey
    val approvedAt: LocalDateTime = confirmResponse.approvedAt
    val seatIds: List<Long> = request.seatIds()
    val addressId: Long = request.addressId()
    val userCouponId: Long = request.userCouponId()
    val concertId: Long = request.concertId()

    init {
        val requestAmount: Long = request.amount()
        val confirmAmount: Long = confirmResponse.totalAmount
        if (requestAmount != confirmAmount) {
            throw IllegalArgumentException(INVALID_AMOUNT)
        }
    }

    companion object {
        fun from(request: PaymentRequest, confirmResponse: TossConfirmResponse): PaymentCommand {
            return PaymentCommand(request, confirmResponse)
        }
    }
}
