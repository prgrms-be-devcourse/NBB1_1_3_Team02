package com.example.book_your_seat.concert_kotlin.controller.dto

import com.example.book_your_seat.concert_kotlin.domain.Concert
import com.example.book_your_seat.review.domain.Review
import com.example.book_your_seat.seat.domain.Seat
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