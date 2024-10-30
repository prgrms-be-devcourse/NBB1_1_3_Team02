package com.example.bookYourSeat.concert.repository

import com.example.bookYourSeat.concert.domain.Concert
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ConcertRepository : JpaRepository<Concert?, Long?>, ConcertJdbcRepository
