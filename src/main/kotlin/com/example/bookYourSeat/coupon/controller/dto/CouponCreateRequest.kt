package com.example.book_your_seat.coupon.controller.dto

import com.example.bookYourSeat.coupon.CouponConst.INVALID_EXPIRATION_DATE_MESSAGE
import com.example.bookYourSeat.coupon.CouponConst.NULL_AMOUNT_MESSAGE
import com.example.bookYourSeat.coupon.CouponConst.NULL_DISCOUNT_RATE_MESSAGE
import com.example.bookYourSeat.coupon.CouponConst.NULL_EXPIRATION_DATE_MESSAGE
import com.example.bookYourSeat.coupon.domain.Coupon
import com.example.bookYourSeat.coupon.domain.DiscountRate
import jakarta.validation.constraints.Future
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

data class CouponCreateRequest(

    @field:NotNull(message = NULL_AMOUNT_MESSAGE)
    val quantity: Int,

    @field:NotNull(message = NULL_DISCOUNT_RATE_MESSAGE)
    val discountRate: Int,

    @field:NotNull(message = NULL_EXPIRATION_DATE_MESSAGE)
    @field:Future(message = INVALID_EXPIRATION_DATE_MESSAGE)
    val expirationDate: LocalDate
) {
    fun toCoupon(): Coupon {
        return Coupon(
            amount = this.quantity,
            discountRate = DiscountRate.findBy(this.discountRate),
            expirationDate = this.expirationDate
        )
    }
}