package com.example.bookYourSeat.concert.service

import com.example.bookYourSeat.concert.controller.dto.AddConcertRequest

interface ConcertCommandService {
    fun add(request: AddConcertRequest): Long
    fun delete(id: Long)
}
