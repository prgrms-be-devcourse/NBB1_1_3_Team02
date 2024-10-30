package com.example.book_your_seat.seat.service.redis.impl

import com.example.book_your_seat.reservation.controller.dto.PaymentRequest
import com.example.book_your_seat.seat.domain.Seat
import com.example.book_your_seat.seat.service.redis.SeatRedisService
import com.example.book_your_seat.seat.SeatConst.ACCEPTABLE_TIMEOUT
import com.example.book_your_seat.seat.SeatConst.SEAT_SOLD
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.TimeUnit

@Component
class SeatRedisServiceImpl(
    private val redisTemplate: RedisTemplate<String, Any>
) : SeatRedisService {

    @Transactional
    override fun cacheSeatIds(seats: List<Seat>, userId: Long) {
        seats.forEach { seat ->
            val redisKey = "seat:${seat.id}"
            try {
                redisTemplate.opsForValue().set(redisKey, userId.toString(), 30, TimeUnit.MINUTES)
            } catch (e: Exception) {
                throw IllegalArgumentException(SEAT_SOLD)
            }
        }
    }

    @Transactional(readOnly = true)
    override fun validateSeat(request: PaymentRequest, userId: Long) {
        request.seatIds.forEach { seatId ->
            val redisKey = "seat:$seatId"

            if (redisTemplate.hasKey(redisKey) == false) {
                throw IllegalArgumentException(ACCEPTABLE_TIMEOUT)
            }

            val storedUserId = redisTemplate.opsForValue().get(redisKey) as? String ?: throw IllegalArgumentException(ACCEPTABLE_TIMEOUT)
            if (userId.toString() != storedUserId) {
                throw IllegalArgumentException(ACCEPTABLE_TIMEOUT)
            }
        }
    }

    @Transactional
    override fun deleteCache(seatId: Long) {
        val key = "seat:$seatId"
        redisTemplate.delete(key)
    }
}