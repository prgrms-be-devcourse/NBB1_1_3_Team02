package com.example.bookYourSeat.reservation.service.dto

data class ReservationsCommand(
    val userId: Long,
    val reservationId: Long,
    val pageSize: Int
) {
    companion object {
        fun from(userId: Long?, pageSize: Int, reservationId: Long?): ReservationsCommand {
            return ReservationsCommand(
                userId ?: throw IllegalArgumentException("userId is required"),
                reservationId ?: throw IllegalArgumentException("reservationId is required"),
                pageSize
            )
        }
    }
}
