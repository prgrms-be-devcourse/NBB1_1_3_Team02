package com.example.bookYourSeat.coupon.manager

import com.example.bookYourSeat.coupon.CouponConst.COUPON_NOT_FOUND
import com.example.bookYourSeat.coupon.domain.Coupon
import com.example.bookYourSeat.coupon.repository.CouponRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(readOnly = true)
class CouponManager(
    private val couponRepository: CouponRepository
) {

    @Transactional
    fun save(coupon: Coupon): Coupon {
        return couponRepository.save(coupon)
    }

    fun findByIdWithPessimistic(couponId: Long): Coupon {
        return couponRepository.findByIdWithPessimistic(couponId)
            .orElseThrow { IllegalArgumentException(COUPON_NOT_FOUND) }
    }

    @Transactional
    fun saveAndFlush(coupon: Coupon) {
        couponRepository.saveAndFlush(coupon)
    }
}