package com.example.bookYourSeat.coupon.facade

import com.example.bookYourSeat.coupon.domain.Coupon
import com.example.bookYourSeat.coupon.domain.DiscountRate
import com.example.bookYourSeat.coupon.domain.UserCoupon
import com.example.bookYourSeat.coupon.manager.UserCouponManager
import com.example.book_your_seat.coupon.controller.dto.CouponDetailResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
open class CouponQueryFacade(
    private val userCouponManager: UserCouponManager
) : CouponQueryService {

    override fun getCouponDetailById(userCouponId: Long): CouponDetailResponse {
        val userCoupon: UserCoupon = userCouponManager.findValidUserCoupon(userCouponId)
        val coupon: Coupon = userCoupon.coupon ?: throw IllegalArgumentException("")
        return CouponDetailResponse.fromCoupon(coupon)
    }

    override fun getDiscountRate(userCouponId: Long): DiscountRate {
        return userCouponManager.findValidUserCoupon(userCouponId)
            .coupon!!
            .discountRate
    }
}