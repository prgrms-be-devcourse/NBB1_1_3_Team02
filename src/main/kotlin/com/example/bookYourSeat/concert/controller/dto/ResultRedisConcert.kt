package com.example.book_your_seat.concert_kotlin.controller.dto

import com.example.bookYourSeat.concert.controller.dto.ConcertListResponse

data class ResultRedisConcert(
    val concertList: List<ConcertListResponse> = emptyList()
)