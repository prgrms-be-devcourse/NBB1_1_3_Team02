package com.example.bookYourSeat.reservation.service

import com.example.bookYourSeat.reservation.repository.ReservationRepository
import org.springframework.stereotype.Service

@Service
class ReservationQueryServiceImpl(
    private val reservationRepository: ReservationRepository // 필요한 의존성을 생성자에 주입
) : ReservationQueryService {
    // 추가적인 메소드 구현이 여기에 들어갈 수 있습니다.
}

