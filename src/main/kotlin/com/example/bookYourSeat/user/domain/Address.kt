package com.example.bookYourSeat.user.domain

import com.example.bookYourSeat.common.entity.BaseEntity
import com.example.bookYourSeat.reservation.domain.Reservation
import jakarta.persistence.*

@Entity
class Address(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    val id: Long = 0L,

    var postcode: String = "",

    detail: String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User? = null
) : BaseEntity() {

    var detail: String = detail.toString()

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    var reservation: Reservation? = null

    init {
        user!!.setAddress(this)
    }

    fun addReservation(reservation: Reservation) {
        this.reservation = reservation
    }
}
