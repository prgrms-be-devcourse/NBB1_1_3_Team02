package com.example.bookYourSeat.coupon.facade

import com.example.bookYourSeat.coupon.domain.DiscountRate
import com.example.book_your_seat.coupon.controller.dto.CouponDetailResponse

interface CouponQueryService {
    fun getCouponDetailById(userCouponId: Long): CouponDetailResponse
    fun getDiscountRate(userCouponId: Long): DiscountRate
}
