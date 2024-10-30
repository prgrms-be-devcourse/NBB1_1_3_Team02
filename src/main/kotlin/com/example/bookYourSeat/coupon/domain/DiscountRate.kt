package com.example.bookYourSeat.coupon.domain

import com.example.bookYourSeat.coupon.CouponConst.PERCENT
import java.math.BigDecimal
import java.math.RoundingMode

enum class DiscountRate(private val value: Int) {
    FIVE(5),
    TEN(10),
    FIFTEEN(15),
    TWENTY(20);

    val stringForm: String
        get() = "$value$PERCENT"

    fun calculateDiscountedPrice(originalPrice: BigDecimal): BigDecimal {
        val hundred = BigDecimal("100")
        val discountValue = BigDecimal(value)
        val percentage = hundred.subtract(discountValue)

        val discountedPercentage = percentage.divide(hundred, 1, RoundingMode.DOWN)
        return originalPrice.multiply(discountedPercentage).setScale(1, RoundingMode.DOWN)
    }

    companion object {
        private const val NOT_VALID_DISCOUNT_RATE = "Not a valid discount rate"

        fun findBy(discountRate: Int): DiscountRate {
            return values().find { it.value == discountRate }
                ?: throw IllegalArgumentException(NOT_VALID_DISCOUNT_RATE)
        }
    }
}