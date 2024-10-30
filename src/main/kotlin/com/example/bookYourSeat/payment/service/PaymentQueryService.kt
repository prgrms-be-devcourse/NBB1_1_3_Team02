package com.example.bookYourSeat.payment.service


import Seat
import com.example.bookYourSeat.payment.domain.Payment
import com.example.bookYourSeat.user.domain.Address
import com.example.bookYourSeat.user.domain.User
import java.math.BigDecimal
import java.util.*

interface PaymentQueryService {
    fun getOriginalPrice(concertId: Long, quantity: Int): BigDecimal
    fun applyCoupon(originalPrice: BigDecimal, userCouponId: Long?): BigDecimal

    fun getUser(userId: Long): User

    fun getAddress(addressId: Long): Address

    fun getSeats(seatsId: List<Long>): List<Seat>

    fun getPayment(paymentId: UUID): Payment
}
