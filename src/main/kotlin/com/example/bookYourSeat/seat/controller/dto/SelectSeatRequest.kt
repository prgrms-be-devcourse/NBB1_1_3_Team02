package com.example.book_your_seat.seat.controller.dto

import com.example.book_your_seat.seat.SeatConst
import jakarta.validation.constraints.NotNull

data class SelectSeatRequest(
    @field:NotNull(message = SeatConst.ENTER_SEATS)
    val seatIds: List<Long>
)