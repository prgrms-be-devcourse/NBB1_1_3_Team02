package com.example.bookYourSeat.payment.service

import com.example.bookYourSeat.payment.domain.Payment
import com.example.bookYourSeat.payment.repository.PaymentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PaymentCommandServiceImpl @Autowired constructor(
    private val paymentRepository: PaymentRepository // nullability를 피하기 위해 lateinit 사용
) : PaymentCommandService {

    override fun savePayment(payment: Payment): Payment {
        return paymentRepository.save(payment)
    }
}
