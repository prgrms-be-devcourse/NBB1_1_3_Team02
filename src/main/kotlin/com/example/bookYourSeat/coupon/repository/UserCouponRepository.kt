package com.example.bookYourSeat.coupon.repository

import com.example.bookYourSeat.coupon.domain.UserCoupon
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface UserCouponRepository : JpaRepository<UserCoupon, Long> {
    fun existsByUserIdAndCouponId(userId: Long, couponId: Long): Boolean
    fun findByIdAndIsUsed(userCouponId: Long, isUsed: Boolean): Optional<UserCoupon>
}