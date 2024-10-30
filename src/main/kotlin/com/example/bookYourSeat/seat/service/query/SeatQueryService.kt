package com.example.book_your_seat.seat.service.query

import Seat
import org.springframework.stereotype.Service

@Service
interface SeatQueryService {
    fun findAllSeats(concertId: Long): List<Seat>

    fun findSeatNumbers(seatIds: List<Long>): List<Int>

    fun getSeatPrice(seatIds: List<Long>): Int

    fun getSeats(seatIds: List<Long>): List<Seat>
}