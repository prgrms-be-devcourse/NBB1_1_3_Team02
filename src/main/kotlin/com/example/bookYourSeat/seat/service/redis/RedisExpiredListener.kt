package com.example.book_your_seat.seat.service.redis

import Seat
import com.example.book_your_seat.seat.repository.SeatRepository
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.stereotype.Component

@Component
class RedisExpiredListener(
    listenerContainer: RedisMessageListenerContainer,
    private val seatRepository: SeatRepository,
    private val seatRedisService: SeatRedisService
) : KeyExpirationEventMessageListener(listenerContainer) {

    override fun onMessage(message: Message, pattern: ByteArray?) {
        val expiredKey = message.toString()
        if (expiredKey.startsWith("seat:")) {
            val seatId = expiredKey.split(":")[1].toLong()
            val seat = seatRepository.findById(seatId)
                .orElseThrow { IllegalArgumentException("Invalid seatId: $seatId") }

            validateSeatSold(seat)
            seatRedisService.deleteCache(seat.id!!)
        }
    }

    private fun validateSeatSold(seat: Seat) {
        if (seat.isSold && seat.reservation == null) {
            seat.releaseSeat()
            seatRepository.save(seat)
        }
    }
}