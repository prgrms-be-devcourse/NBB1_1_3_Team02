package com.example.book_your_seat.coupon.controller.dto

import com.example.bookYourSeat.coupon.domain.Coupon
import java.time.LocalDate

data class CouponDetailResponse(
    val discountRate: String,
    val issuedAt: LocalDate,
    val expirationDate: LocalDate
) {
    companion object {
        fun fromCoupon(coupon: Coupon): CouponDetailResponse {
            return CouponDetailResponse(
                discountRate = coupon.discountRate.stringForm,
                issuedAt = coupon.createdAt?.toLocalDate() ?: LocalDate.now(),
                expirationDate = coupon.expirationDate
            )
        }
    }
}