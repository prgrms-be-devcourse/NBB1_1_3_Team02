package com.example.book_your_seat.seat.service.query

import com.example.book_your_seat.concert.domain.Concert
import com.example.book_your_seat.seat.domain.Seat
import com.example.book_your_seat.seat.repository.SeatRepository
import com.example.book_your_seat.concert.repository.ConcertRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class SeatQueryServiceImpl(
    private val seatRepository: SeatRepository,
    private val concertRepository: ConcertRepository
) : SeatQueryService {

    override fun findAllSeats(concertId: Long): List<Seat> {
        val concert = concertRepository.findById(concertId)
            .orElseThrow { IllegalArgumentException(INVALID_CONCERT) }

        validateConcertDate(concert)
        return seatRepository.findByConcertId(concertId)
    }

    override fun findSeatNumbers(seatIds: List<Long>): List<Int> {
        return seatRepository.findValidSeats(seatIds)
            .map { it.seatNumber }
    }

    override fun getSeatPrice(seatIds: List<Long>): Int {
        val seats = seatRepository.findAllById(seatIds)
        val concertPrice = seats.first().concert.price

        return seats.sumOf { seat -> seat.zone.applyZonePrice(concertPrice) }
    }

    override fun getSeats(seatIds: List<Long>): List<Seat> {
        return seatRepository.findAllById(seatIds)
    }

    private fun validateConcertDate(concert: Concert) {
        val reservationDeadline = concert.startDate.minusDays(1)
        if (java.time.LocalDate.now().isAfter(reservationDeadline)) {
            throw IllegalArgumentException(INVALID_CONCERT)
        }
    }
}
