package com.example.bookYourSeat.reservation.service

import com.example.bookYourSeat.reservation.domain.Reservation
import com.example.bookYourSeat.reservation.repository.ReservationRepository
import org.springframework.stereotype.Service

@Service
class ReservationCommandServiceImpl(
    private val reservationRepository: ReservationRepository // 생성자 주입
) : ReservationCommandService {

    override fun saveReservation(reservation: Reservation): Reservation { // nullable 제거
        return reservationRepository.save(reservation)
    }
}
