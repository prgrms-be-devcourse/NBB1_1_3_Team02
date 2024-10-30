package com.example.book_your_seat.seat.service.facade

import com.example.book_your_seat.reservation.domain.Reservation
import com.example.book_your_seat.seat.controller.dto.SelectSeatRequest
import com.example.book_your_seat.seat.controller.dto.SelectSeatResponse
import com.example.book_your_seat.seat.controller.dto.SeatResponse
import com.example.book_your_seat.seat.domain.Seat

interface SeatFacade {
    fun findAllSeats(concertId: Long): List<SeatResponse>

    fun selectSeat(request: SelectSeatRequest, userId: Long): SelectSeatResponse

    fun selectSeatRedisson(request: SelectSeatRequest, userId: Long): SelectSeatResponse

    fun getSeats(seatIds: List<Long>): List<Seat>

    fun seatReservationComplete(seats: List<Seat>, reservation: Reservation)
}