package com.example.book_your_seat.seat.service.command

import com.example.book_your_seat.reservation.domain.Reservation
import com.example.book_your_seat.seat.controller.dto.SelectSeatRequest
import com.example.book_your_seat.seat.domain.Seat
import com.example.book_your_seat.seat.repository.SeatRepository
import com.example.book_your_seat.seat.SeatConst
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class SeatCommandServiceImpl(
    private val seatRepository: SeatRepository
) : SeatCommandService {

    private val log = LoggerFactory.getLogger(SeatCommandServiceImpl::class.java)

    override fun selectSeat(request: SelectSeatRequest): List<Seat> {
        val seats = seatRepository.findAllByIdWithLock(request.seatIds)

        seats.forEach { seat ->
            if (seat.isSold) {
                log.info("seatId = {}", seat.id)
                log.info("seat isSold = {}", seat.isSold)
                throw IllegalArgumentException(SeatConst.SEAT_SOLD)
            }
            seat.selectSeat()
        }
        return seatRepository.saveAll(seats)
    }

    override fun selectSeatRedisson(request: SelectSeatRequest): List<Seat> {
        val seats = seatRepository.findAllById(request.seatIds)

        seats.forEach { seat ->
            require(!seat.isSold) { SeatConst.SEAT_SOLD }
            seat.selectSeat()
        }
        return seatRepository.saveAll(seats)
    }

    override fun seatReservationComplete(seats: List<Seat>, reservation: Reservation) {
        seats.forEach { seat ->
            seat.assignReservation(reservation)
        }

        seatRepository.saveAll(seats)
    }
}