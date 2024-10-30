package com.example.bookYourSeat.coupon.facade

import UserCouponResponse
import com.example.bookYourSeat.coupon.controller.dto.UserCouponRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

interface UserCouponService {
    fun getUserCoupons(
        userCouponRequest: UserCouponRequest,
        userId: Long,
        pageable: Pageable
    ): Slice<UserCouponResponse>
}