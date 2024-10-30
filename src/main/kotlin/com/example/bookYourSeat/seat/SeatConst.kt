package com.example.book_your_seat.seat

object SeatConst {
    const val INVALID_ADDRESS = "addressId를 입력하세요"
    const val ENTER_SEATS = "seats를 입력하세요"

    const val SEAT_SOLD = "이미 예약된 좌석 입니다."
    const val REDISSON_LOCK_KEY = "LOCK_SEAT:"

    const val ACCEPTABLE_TIMEOUT = "결재 가능 시간을 초과 했습니다."

    const val SPECIAL_RATIO = 3
    const val PREMIUM_RATIO = 2
    const val NORMAL_RATIO = 1
}