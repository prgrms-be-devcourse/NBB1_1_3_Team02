package com.example.bookYourSeat.payment.service

import com.example.bookYourSeat.payment.domain.Payment
import org.springframework.stereotype.Service

@Service
interface PaymentCommandService {
    fun savePayment(payment: Payment): Payment // nullability 제거
}
