package com.example.book_your_seat.seat.repository

import com.example.book_your_seat.seat.domain.Seat
import org.springframework.stereotype.Repository

@Repository
interface SeatQueryRepository {
    fun findValidSeats(seats: List<Long>): List<Seat>
}