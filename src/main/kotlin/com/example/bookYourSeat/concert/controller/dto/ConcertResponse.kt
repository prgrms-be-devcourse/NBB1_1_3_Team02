package com.example.bookYourSeat.concert.controller.dto

import Seat
import com.example.bookYourSeat.concert.domain.Concert
import com.example.bookYourSeat.review.domain.Review
import java.time.LocalDate
import java.time.LocalDateTime

data class ConcertResponse(
    val id: Long,
    val title: String,
    val totalStock: Int,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val price: Int,
    val startHour: Int,
    val reservationStartAt: LocalDateTime,
    val reviews: List<Review> = emptyList(),
    val seats: List<Seat> = emptyList()
) {
    companion object {
        fun from(concert: Concert): ConcertResponse {
            return ConcertResponse(
                id = concert.id ?: 0L, // nullable 처리
                title = concert.title,
                totalStock = concert.totalStock,
                startDate = concert.startDate,
                endDate = concert.endDate,
                price = concert.price,
                startHour = concert.startHour,
                reservationStartAt = concert.reservationStartAt,
                reviews = concert.reviews,
                seats = concert.seats
            )
        }
    }
}