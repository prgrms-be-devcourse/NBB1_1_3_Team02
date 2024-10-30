package com.example.book_your_seat.concert_kotlin.service

import com.example.book_your_seat.concert_kotlin.controller.dto.AddConcertRequest

interface ConcertCommandService {
    fun add(request: AddConcertRequest): Long
    fun delete(id: Long)
}
