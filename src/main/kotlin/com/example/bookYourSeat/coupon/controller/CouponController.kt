package com.example.bookYourSeat.coupon.controller

import UserCouponResponse
import com.example.bookYourSeat.coupon.controller.dto.UserCouponIdResponse
import com.example.bookYourSeat.coupon.controller.dto.UserCouponRequest
import com.example.bookYourSeat.coupon.facade.CouponCommandService
import com.example.bookYourSeat.coupon.facade.UserCouponService
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/coupons")
class CouponController(
    private val couponCommandService: CouponCommandService,
    private val userCouponService: UserCouponService
) {

    @PostMapping("/{couponId}")
    fun issueCoupon(
        @LoginUser user: User,
        @PathVariable("couponId") couponId: Long
    ): ResponseEntity<UserCouponIdResponse> {
        val userCouponIdResponse = couponCommandService.issueCouponWithPessimistic(user.id, couponId)
        return ResponseEntity.status(HttpStatus.OK).body(userCouponIdResponse)
    }

    @GetMapping("/user")
    fun getUserCoupons(
        @LoginUser user: User,
        @RequestBody userCouponRequest: UserCouponRequest,
        pageable: Pageable
    ): Slice<UserCouponResponse> {
        return userCouponService.getUserCoupons(userCouponRequest, user.id, pageable)
    }
}