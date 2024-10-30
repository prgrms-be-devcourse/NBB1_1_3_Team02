package com.example.book_your_seat.seat.domain

import com.example.book_your_seat.seat.SeatConst

enum class Zone(private val priceMultiplier: (Int) -> Int) {
    SPECIAL({ price -> price * SeatConst.SPECIAL_RATIO }),
    PREMIUM({ price -> price * SeatConst.PREMIUM_RATIO }),
    NORMAL({ price -> price * SeatConst.NORMAL_RATIO });

    fun applyZonePrice(price: Int): Int {
        return priceMultiplier(price)
    }

    companion object {
        fun setZone(seatNumber: Int): Zone {
            return when {
                seatNumber <= 30 -> SPECIAL
                seatNumber <= 60 -> PREMIUM
                else -> NORMAL
            }
        }
    }
}