package com.example.bookYourSeat.reservation.repository

import com.example.bookYourSeat.reservation.domain.Reservation
import org.springframework.data.jpa.repository.JpaRepository

interface ReservationRepository : JpaRepository<Reservation, Long>

