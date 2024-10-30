package com.example.bookYourSeat.concert.controller.dto

import com.example.book_your_seat.concert_kotlin.ConcertConst
import com.example.bookYourSeat.concert.domain.Concert
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

data class AddConcertRequest(
    @field:NotBlank(message = ConcertConst.ENTER_CONCERT_TITLE)
    val title: String,

    @field:NotNull(message = ConcertConst.ENTER_CONCERT_START_DATE)
    val startDate: LocalDate,

    @field:NotNull(message = ConcertConst.ENTER_CONCERT_END_DATE)
    val endDate: LocalDate,

    @field:NotNull(message = ConcertConst.ENTER_CONCERT_PRICE)
    val price: Int,

    @field:NotNull(message = ConcertConst.ENTER_CONCERT_START_HOUR)
    @field:Min(value = 0, message = ConcertConst.INVALID_CONCERT_START_HOUR)
    @field:Max(value = 24, message = ConcertConst.INVALID_CONCERT_START_HOUR)
    val startHour: Int
) {
    companion object {
        fun AddConcertRequest.toConcert(): Concert {
            return Concert(
                title = this.title,
                startDate = this.startDate,
                endDate = this.endDate,
                price = this.price,
                startHour = this.startHour
            )
        }
    }
}