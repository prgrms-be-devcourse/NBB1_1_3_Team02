package com.example.book_your_seat.seat.service.command

import com.example.book_your_seat.reservation.domain.Reservation
import com.example.book_your_seat.seat.controller.dto.SelectSeatRequest
import com.example.book_your_seat.seat.domain.Seat

interface SeatCommandService {
    fun selectSeat(request: SelectSeatRequest): List<Seat>

    fun selectSeatRedisson(request: SelectSeatRequest): List<Seat>

    fun seatReservationComplete(seats: List<Seat>, reservation: Reservation)
}