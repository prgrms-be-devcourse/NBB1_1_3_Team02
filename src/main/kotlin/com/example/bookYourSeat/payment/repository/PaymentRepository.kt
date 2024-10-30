package com.example.bookYourSeat.payment.repository

import com.example.bookYourSeat.payment.domain.Payment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PaymentRepository : JpaRepository<Payment, UUID>

