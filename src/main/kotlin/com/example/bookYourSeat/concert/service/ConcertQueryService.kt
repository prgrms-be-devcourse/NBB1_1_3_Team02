package com.example.book_your_seat.concert_kotlin.service

import com.example.book_your_seat.concert_kotlin.controller.dto.ConcertListResponse
import com.example.book_your_seat.concert_kotlin.controller.dto.ConcertResponse
import com.example.book_your_seat.concert_kotlin.controller.dto.ResultRedisConcert

interface ConcertQueryService {
    fun findAll(): List<ConcertResponse>
    fun findById(id: Long): ConcertResponse
    fun findAllConcertList(): List<ConcertListResponse>
    fun findUsedRedisAllConcertList(): ResultRedisConcert
}
