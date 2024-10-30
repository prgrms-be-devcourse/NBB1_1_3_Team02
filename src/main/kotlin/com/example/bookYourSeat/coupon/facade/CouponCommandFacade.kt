package com.example.bookYourSeat.coupon.facade

import com.example.bookYourSeat.aop.distributedlock.DistributedLock
import com.example.bookYourSeat.coupon.controller.dto.CouponResponse
import com.example.bookYourSeat.coupon.controller.dto.UserCouponIdResponse
import com.example.bookYourSeat.coupon.domain.UserCoupon
import com.example.bookYourSeat.coupon.manager.CouponManager
import com.example.bookYourSeat.coupon.manager.UserCouponManager
import com.example.bookYourSeat.user.service.query.UserQueryService
import com.example.book_your_seat.coupon.controller.dto.CouponCreateRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
open class CouponCommandFacade(
    private val userQueryService: UserQueryService,
    private val couponManager: CouponManager,
    private val userCouponManager: UserCouponManager
) : CouponCommandService {

    override fun createCoupon(couponCreateRequest: CouponCreateRequest): CouponResponse {
        val coupon = couponCreateRequest.toCoupon()
        val savedCoupon = couponManager.save(coupon)
        return CouponResponse(savedCoupon.id!!)
    }

    @DistributedLock(key = "coupon_lock")
    override fun issueCouponWithPessimistic(userId: Long, couponId: Long): UserCouponIdResponse {
        val user = userQueryService.getUserByUserId(userId)
        val coupon = couponManager.findByIdWithPessimistic(couponId)

        userCouponManager.checkAlreadyIssuedUser(userId, couponId)

        coupon.issue()
        couponManager.saveAndFlush(coupon)

        val userCoupon = userCouponManager.save(UserCoupon(user, coupon))
        return UserCouponIdResponse(userCoupon.id!!)
    }

    override fun useUserCoupon(userCouponId: Long) {
        val validUserCoupon = userCouponManager.findValidUserCoupon(userCouponId)
        userCouponManager.updateUserCoupon(validUserCoupon)
    }
}