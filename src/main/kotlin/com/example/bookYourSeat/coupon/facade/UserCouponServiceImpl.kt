package com.example.bookYourSeat.coupon.facade

import UserCouponResponse
import com.example.bookYourSeat.coupon.controller.dto.UserCouponRequest
import com.example.bookYourSeat.coupon.repository.CouponRepository
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.stereotype.Service

@Service
class UserCouponServiceImpl(
    private val couponRepository: CouponRepository
) : UserCouponService {

    override fun getUserCoupons(
        userCouponRequest: UserCouponRequest,
        userId: Long,
        pageable: Pageable
    ): Slice<UserCouponResponse> {
        return couponRepository.selectUserCoupons(userCouponRequest, userId, pageable)
    }
}