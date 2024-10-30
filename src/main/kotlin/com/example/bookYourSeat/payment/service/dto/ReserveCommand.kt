package com.example.bookYourSeat.payment.service.dto

import com.example.bookYourSeat.payment.controller.dto.request.ReserveRequest

data class ReserveCommand private constructor(
    val concertId: Long,
    val userId: Long,
    val seatsId: List<Long>,
    val userCouponId: Long,
    val addressId: Long
) {
    companion object {
        fun from(concertId: Long?, userId: Long?, request: ReserveRequest): ReserveCommand {
            return ReserveCommand(
                concertId ?: throw IllegalArgumentException("Concert ID is required"),
                userId ?: throw IllegalArgumentException("User ID is required"),
                request.seatsId, // 직접 접근
                request.userCouponId,
                request.addressId
            )
        }
    }
}