package com.example.bookYourSeat.reservation.contorller.dto

import Seat
import com.example.bookYourSeat.reservation.domain.ReservationStatus

data class ConfirmationReservationResponse(
    val reservationId: Long,
    val status: ReservationStatus,
    val finalPrice: Int,
    val address: String,
    val seats: List<Seat>
)
