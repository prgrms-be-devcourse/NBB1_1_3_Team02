package com.example.book_your_seat.seat.service.redis

import Seat
import com.example.bookYourSeat.reservation.contorller.dto.PaymentRequest

interface SeatRedisService {
    fun cacheSeatIds(seats: List<Seat>, userId: Long)

    fun validateSeat(request: PaymentRequest, userId: Long)

    fun deleteCache(seatId: Long)
}