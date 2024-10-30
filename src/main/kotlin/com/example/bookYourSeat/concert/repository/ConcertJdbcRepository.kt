package com.example.book_your_seat.concert_kotlin.repository

import com.example.book_your_seat.concert_kotlin.domain.Concert
import org.springframework.stereotype.Repository

@Repository
interface ConcertJdbcRepository {
    fun saveBulkData(concert: Concert): Long

    fun deleteBulkData(concertId: Long?)
}
