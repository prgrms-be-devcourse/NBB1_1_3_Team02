package com.example.bookYourSeat.coupon.facade

import com.example.bookYourSeat.coupon.controller.dto.CouponResponse
import com.example.bookYourSeat.coupon.controller.dto.UserCouponIdResponse
import com.example.book_your_seat.coupon.controller.dto.CouponCreateRequest

interface CouponCommandService {
    fun createCoupon(couponCreateRequest: CouponCreateRequest): CouponResponse
    fun issueCouponWithPessimistic(userId: Long, couponId: Long): UserCouponIdResponse
    fun useUserCoupon(userCouponId: Long)
}