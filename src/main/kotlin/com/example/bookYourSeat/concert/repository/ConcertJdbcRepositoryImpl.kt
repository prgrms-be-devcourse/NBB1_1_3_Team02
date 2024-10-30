package com.example.bookYourSeat.concert.repository

import com.example.book_your_seat.concert_kotlin.ConcertConst
import com.example.bookYourSeat.concert.domain.Concert
import org.springframework.jdbc.core.BatchPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import java.sql.*

@Repository
class ConcertJdbcRepositoryImpl(
    private val jdbcTemplate: JdbcTemplate
) : ConcertJdbcRepository {

    override fun saveBulkData(concert: Concert): Long {
        val concertId = saveConcert(concert)
        saveBulkSeat(concert, concertId)
        return concertId
    }

    override fun deleteBulkData(concertId: Long?) {
        if (concertId == null) return

        val seatSql = "DELETE FROM seat WHERE concert_id = ?"
        val concertSql = "DELETE FROM concert WHERE concert_id = ?"

        jdbcTemplate.update(seatSql, concertId)
        jdbcTemplate.update(concertSql, concertId)
    }

    private fun saveConcert(concert: Concert): Long {
        val concertSql = """
            INSERT INTO concert (
                title, 
                start_date, 
                end_date, 
                price, 
                start_hour, 
                total_stock, 
                reservation_start_at
            ) VALUES (?, ?, ?, ?, ?, ?, ?)
        """.trimIndent()

        val keyHolder: KeyHolder = GeneratedKeyHolder()

        jdbcTemplate.update({ con: Connection ->
            val ps = con.prepareStatement(concertSql, Statement.RETURN_GENERATED_KEYS)
            ps.setString(1, concert.title)
            ps.setDate(2, Date.valueOf(concert.startDate))
            ps.setDate(3, Date.valueOf(concert.endDate))
            ps.setInt(4, concert.price)
            ps.setInt(5, concert.startHour)
            ps.setInt(6, ConcertConst.TOTAL_STOCK)
            ps.setTimestamp(7, Timestamp.valueOf(concert.reservationStartAt))
            ps
        }, keyHolder)

        return keyHolder.key?.toLong()
            ?: throw IllegalStateException("Failed to retrieve generated key for Concert")
    }

    private fun saveBulkSeat(concert: Concert, concertId: Long) {
        val seatSql = """
            INSERT INTO seat (concert_id, is_sold, seat_number) 
            VALUES (?, ?, ?)
        """.trimIndent()

        jdbcTemplate.batchUpdate(seatSql, object : BatchPreparedStatementSetter {
            override fun setValues(ps: PreparedStatement, i: Int) {
                val seat = concert.seats[i]
                ps.setLong(1, concertId)
                ps.setBoolean(2, seat.isSold)
                ps.setInt(3, seat.seatNumber)
            }

            override fun getBatchSize(): Int = concert.seats.size
        })
    }
}