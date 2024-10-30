package com.example.bookYourSeat.reservation.contorller.dto

import com.example.bookYourSeat.reservation.domain.ReservationStatus
import com.example.book_your_seat.seat.domain.Seat

data class ConfirmationReservationResponse(
    val reservationId: Long,
    val status: ReservationStatus,
    val finalPrice: Int,
    val address: String,
    val seats: List<Seat>
)
