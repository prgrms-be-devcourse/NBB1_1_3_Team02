package com.example.book_your_seat.seat.controller.dto

import Seat

data class SelectSeatResponse(
    val seatIds: List<Long>
) {
    companion object {
        fun fromSeats(seats: List<Seat?>): SelectSeatResponse {
            val ids : List<Long> = seats.filterNotNull().map { it.id!! }
            return SelectSeatResponse(ids)
        }
    }
}
