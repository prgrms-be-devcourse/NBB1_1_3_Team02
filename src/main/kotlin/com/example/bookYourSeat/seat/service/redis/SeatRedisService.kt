package com.example.book_your_seat.seat.service.redis

import com.example.book_your_seat.reservation.controller.dto.PaymentRequest
import com.example.book_your_seat.seat.domain.Seat

interface SeatRedisService {
    fun cacheSeatIds(seats: List<Seat>, userId: Long)

    fun validateSeat(request: PaymentRequest, userId: Long)

    fun deleteCache(seatId: Long)
}