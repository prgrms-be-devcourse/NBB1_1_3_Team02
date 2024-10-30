package com.example.bookYourSeat.coupon.repository

import UserCouponResponse
import com.example.bookYourSeat.coupon.controller.dto.UserCouponRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

interface CouponRepositoryCustom {
    fun selectUserCoupons(
        userCouponRequest: UserCouponRequest,
        memberId: Long,
        pageable: Pageable
    ): Slice<UserCouponResponse>
}