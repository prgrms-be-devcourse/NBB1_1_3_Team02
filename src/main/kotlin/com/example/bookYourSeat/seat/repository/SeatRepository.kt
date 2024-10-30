package com.example.book_your_seat.seat.repository

import Seat
import jakarta.persistence.LockModeType
import jakarta.persistence.QueryHint
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.jpa.repository.QueryHints

interface SeatRepository : JpaRepository<Seat, Long> {

    @Query(value = "SELECT * FROM Seat WHERE concert_id = :concertId", nativeQuery = true)
    fun findByConcertId(concertId: Long): List<Seat>

    @Lock(LockModeType.PESSIMISTIC_READ)
    @QueryHints(QueryHint(name = "javax.persistence.lock.timeout", value = "3000"))
    @Query("SELECT s FROM Seat s WHERE s.id IN :seatIds")
    fun findAllByIdWithLock(seatIds: List<Long>): List<Seat>

    @Query("SELECT s FROM Seat s WHERE s.id IN :seatIds")
    fun findAllById(seatIds: List<Long>): List<Seat>

    @Query("SELECT s FROM Seat s WHERE s.id IN :seatsId AND s.isSold = false")
    fun findValidSeats(seatsId: List<Long>): List<Seat>
}