package com.example.book_your_seat.coupon.controller

import com.example.bookYourSeat.coupon.controller.dto.CouponResponse
import com.example.bookYourSeat.coupon.facade.CouponCommandService
import com.example.book_your_seat.coupon.controller.dto.CouponCreateRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin/api/v1/coupons")
class AdminCouponController(
    private val couponCommandService: CouponCommandService
) {

    @PostMapping
    fun addCoupon(
        @RequestBody couponCreateRequest: CouponCreateRequest
    ): ResponseEntity<CouponResponse> {
        val couponResponse = couponCommandService.createCoupon(couponCreateRequest)
        return ResponseEntity.status(HttpStatus.CREATED).body(couponResponse)
    }
}