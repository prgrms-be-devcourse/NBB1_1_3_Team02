package com.example.book_your_seat.seat.service.command

import Seat
import com.example.bookYourSeat.reservation.domain.Reservation
import com.example.book_your_seat.seat.controller.dto.SelectSeatRequest

interface SeatCommandService {
    fun selectSeat(request: SelectSeatRequest): List<Seat>

    fun selectSeatRedisson(request: SelectSeatRequest): List<Seat>

    fun seatReservationComplete(seats: List<Seat>, reservation: Reservation)
}