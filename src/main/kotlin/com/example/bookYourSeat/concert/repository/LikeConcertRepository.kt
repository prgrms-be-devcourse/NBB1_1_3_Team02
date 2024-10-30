package com.example.bookYourSeat.concert.repository

import com.example.bookYourSeat.concert.domain.LikeConcert
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LikeConcertRepository : JpaRepository<LikeConcert, Long>
