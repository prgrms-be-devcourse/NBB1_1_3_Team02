package com.example.book_your_seat.seat.controller.dto

import Seat

data class SeatResponse (val seatId: Long, val isSold: Boolean) {
    companion object {
        fun from(seat: Seat): SeatResponse {
            return SeatResponse(seatId = seat.id?: 0L, isSold = seat.isSold)
        }
    }
}