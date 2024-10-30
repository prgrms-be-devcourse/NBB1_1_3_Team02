package com.example.bookYourSeat.coupon.manager

import com.example.bookYourSeat.coupon.CouponConst.ALREADY_ISSUED_USER
import com.example.bookYourSeat.coupon.domain.UserCoupon
import com.example.bookYourSeat.coupon.repository.UserCouponRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(readOnly = true)
class UserCouponManager(
    private val userCouponRepository: UserCouponRepository
) {

    fun checkAlreadyIssuedUser(userId: Long, couponId: Long) {
        if (userCouponRepository.existsByUserIdAndCouponId(userId, couponId)) {
            throw IllegalArgumentException(ALREADY_ISSUED_USER)
        }
    }

    @Transactional
    fun save(userCoupon: UserCoupon): UserCoupon {
        return userCouponRepository.save(userCoupon)
    }

    fun findValidUserCoupon(userCouponId: Long): UserCoupon {
        return userCouponRepository.findByIdAndIsUsed(userCouponId, false)
            .orElseThrow { IllegalArgumentException("INVALID_USER_COUPON") }
    }

    @Transactional
    fun updateUserCoupon(userCoupon: UserCoupon) {
        userCoupon.setUsed()
        userCouponRepository.save(userCoupon)
    }
}