package com.example.bookYourSeat.user.domain

import com.example.bookYourSeat.common.entity.BaseEntity
import com.example.bookYourSeat.reservation.domain.Reservation
import jakarta.persistence.*

@Entity
class Address(
    postcode: String? = "",
    detail: String? = "",
    user: User? = null
) : BaseEntity() {

    constructor() : this(null) {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    val id: Long? = null

    var postcode: String? = postcode
        protected set

    var detail: String = detail.toString()
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User? = user
        protected set

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
