package com.example.bookYourSeat.concert.repository

import com.example.bookYourSeat.concert.domain.Concert
import org.springframework.stereotype.Repository

@Repository
interface ConcertJdbcRepository {
    fun saveBulkData(concert: Concert): Long

    fun deleteBulkData(concertId: Long?)
}
