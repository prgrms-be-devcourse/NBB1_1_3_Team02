package com.example.book_your_seat.concert_kotlin.controller.dto

import com.example.book_your_seat.concert_kotlin.domain.Concert
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import java.time.LocalDate

data class ConcertListResponse(
    val id: Long,
    val title: String,
    val totalStock: Int,

    @JsonDeserialize(using = LocalDateDeserializer::class)
    @JsonSerialize(using = LocalDateSerializer::class)
    val startDate: LocalDate,

    @JsonDeserialize(using = LocalDateDeserializer::class)
    @JsonSerialize(using = LocalDateSerializer::class)
    val endDate: LocalDate,

    val price: Int,
    val startHour: Int
) {
    companion object {
        fun from(concert: Concert): ConcertListResponse {
            return ConcertListResponse(
                id = concert.id ?: 0L,
                title = concert.title,
                totalStock = concert.totalStock,
                startDate = concert.startDate,
                endDate = concert.endDate,
                price = concert.price,
                startHour = concert.startHour
            )
        }
    }
}