package com.example.book_your_seat.concert_kotlin.controller.dto

data class ResultRedisConcert(
    val concertList: List<ConcertListResponse> = emptyList()
)