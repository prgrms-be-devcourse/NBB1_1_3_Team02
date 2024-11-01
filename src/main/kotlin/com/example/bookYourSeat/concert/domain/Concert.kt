package com.example.bookYourSeat.concert.domain

import Seat
import com.example.bookYourSeat.common.entity.BaseEntity
import com.example.bookYourSeat.review.domain.Review
import com.example.book_your_seat.concert_kotlin.ConcertConst
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
class Concert(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "concert_id")
    var id: Long? = null,

    val title: String = "",
    val startDate: LocalDate = LocalDate.now(),
    val endDate: LocalDate = LocalDate.now(),
    val price: Int = 0,
    val startHour: Int = 0,

    val totalStock: Int = ConcertConst.TOTAL_STOCK,

    val reservationStartAt: LocalDateTime = setReservationTime(startDate),

    @OneToMany(mappedBy = "concert", cascade = [CascadeType.ALL], orphanRemoval = true)
    val likeConcerts: MutableList<LikeConcert> = mutableListOf(),

    @OneToMany(mappedBy = "concert", cascade = [CascadeType.ALL], orphanRemoval = true)
    val reviews: MutableList<Review> = mutableListOf(),

    @OneToMany(mappedBy = "concert", cascade = [CascadeType.ALL], orphanRemoval = true)
    var seats: MutableList<Seat> = mutableListOf(),


) : BaseEntity() {

    init {
        initializeSeats()
    }

    private fun setReservationTime(startDate: LocalDate): LocalDateTime {
        return LocalDateTime.of(
            startDate.year,
            startDate.month,
            startDate.dayOfMonth,
            ConcertConst.RESERVATION_START_HOUR,
            ConcertConst.RESERVATION_START_MINUTE,
            ConcertConst.RESERVATION_START_SECOND
        ).minusWeeks(1)
    }

    private fun initializeSeats() {
        (1..totalStock).forEach { i ->
            addSeat(Seat(this@Concert, i))
        }
    }

    fun addLikeConcert(likeConcert: LikeConcert) {
        likeConcerts.add(likeConcert)
    }

    fun addReview(review: Review) {
        reviews.add(review)
    }

    fun addSeat(seat: Seat) {
        seats.add(seat)
    }
}

fun setReservationTime(startDate: LocalDate): LocalDateTime {
    return LocalDateTime.of(
        startDate.year,
        startDate.month,
        startDate.dayOfMonth,
        ConcertConst.RESERVATION_START_HOUR,
        ConcertConst.RESERVATION_START_MINUTE,
        ConcertConst.RESERVATION_START_SECOND
    ).minusWeeks(1)
}
