package com.example.book_your_seat.concert_kotlin.repository

import com.example.book_your_seat.concert_kotlin.domain.Concert
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ConcertRepository : JpaRepository<Concert?, Long?>, ConcertJdbcRepository
