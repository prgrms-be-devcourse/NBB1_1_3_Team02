package com.example.bookYourSeat.reservation.service

import com.example.bookYourSeat.reservation.domain.Reservation

interface ReservationCommandService {
    fun saveReservation(reservation: Reservation): Reservation // nullable 제거
}
