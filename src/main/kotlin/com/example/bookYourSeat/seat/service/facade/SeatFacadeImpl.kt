package com.example.book_your_seat.seat.service.facade

import com.example.book_your_seat.queue.QueueConst.NOT_IN_PROCESSING_QUEUE
import com.example.book_your_seat.reservation.domain.Reservation
import com.example.book_your_seat.seat.controller.dto.SelectSeatRequest
import com.example.book_your_seat.seat.controller.dto.SelectSeatResponse
import com.example.book_your_seat.seat.controller.dto.SeatResponse
import com.example.book_your_seat.seat.domain.Seat
import com.example.book_your_seat.seat.service.command.SeatCommandService
import com.example.book_your_seat.seat.service.query.SeatQueryService
import com.example.book_your_seat.redis.SeatRedisService
import com.example.book_your_seat.redis.QueueRedisRepository
import org.springframework.stereotype.Service

@Service
class SeatFacadeImpl(
    private val seatCommandService: SeatCommandService,
    private val seatQueryService: SeatQueryService,
    private val redisService: SeatRedisService,
    private val queueRedisRepository: QueueRedisRepository
) : SeatFacade {

    override fun findAllSeats(concertId: Long): List<SeatResponse> {
        return seatQueryService.findAllSeats(concertId)
            .map { seat -> SeatResponse.from(seat) }
    }

    override fun selectSeat(request: SelectSeatRequest, userId: Long): SelectSeatResponse {
        checkInProcessingQueue(userId)

        val seats = seatCommandService.selectSeat(request)

        redisService.cacheSeatIds(seats, userId)

        return SelectSeatResponse.fromSeats(seats)
    }

    @SeatLock
    override fun selectSeatRedisson(request: SelectSeatRequest, userId: Long): SelectSeatResponse {
        checkInProcessingQueue(userId)

        val seats = seatCommandService.selectSeatRedisson(request)

        redisService.cacheSeatIds(seats, userId)

        return SelectSeatResponse.fromSeats(seats)
    }

    private fun checkInProcessingQueue(userId: Long) {
        if (!queueRedisRepository.isInProcessingQueue(userId)) {
            throw IllegalArgumentException(NOT_IN_PROCESSING_QUEUE)
        }
    }

    override fun getSeats(seatIds: List<Long>): List<Seat> {
        return seatQueryService.getSeats(seatIds)
    }

    override fun seatReservationComplete(seats: List<Seat>, reservation: Reservation) {
        seatCommandService.seatReservationComplete(seats, reservation)
    }
}