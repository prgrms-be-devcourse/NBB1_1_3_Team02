package com.example.book_your_seat.seat.controller.dto

import com.example.book_your_seat.seat.domain.Seat

data class SelectSeatResponse(
    val seatIds: List<Long>
) {
    companion object {
        fun fromSeats(seats: List<Seat?>): SelectSeatResponse {
            val ids = seats.filterNotNull().map { it.getId() }
            return SelectSeatResponse(ids)
        }
    }
}
