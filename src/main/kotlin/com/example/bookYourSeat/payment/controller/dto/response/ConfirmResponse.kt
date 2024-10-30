package com.example.bookYourSeat.payment.controller.dto.response

import com.example.bookYourSeat.reservation.domain.ReservationStatus

data class ConfirmResponse(
    val userId: Long,
    val reservationId: Long,
    val concludePrice: Long,
    val status: ReservationStatus,
    val seatsId: List<Long>,
    val concertTitle: String,
    val concertStartHour: Int,
    val seatNumbers: List<Int>
)
