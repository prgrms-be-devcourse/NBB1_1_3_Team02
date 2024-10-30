package com.example.bookYourSeat.payment.service.facade

import com.example.book_your_seat.payment.controller.dto.request.FinalPriceRequest
import com.example.book_your_seat.payment.controller.dto.response.ConfirmResponse
import com.example.book_your_seat.payment.controller.dto.response.FinalPriceResponse
import com.example.book_your_seat.payment.service.dto.PaymentCommand
import org.springframework.stereotype.Service

@Service
interface PaymentFacade {
    fun processPayment(paymentCommand: PaymentCommand, userId: Long, token: String): ConfirmResponse

    fun getFinalPrice(request: FinalPriceRequest): FinalPriceResponse
}
