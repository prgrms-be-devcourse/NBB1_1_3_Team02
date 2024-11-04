package com.example.bookYourSeat.reservation.domain

import Seat
import com.example.bookYourSeat.payment.domain.Payment
import com.example.bookYourSeat.user.domain.Address
import com.example.bookYourSeat.user.domain.User
import jakarta.persistence.*

@Entity
class Reservation(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    var id: Long = 0L,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private var user: User? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private var address: Address? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private var payment: Payment? = null,

    @OneToMany(mappedBy = "reservation", cascade = [CascadeType.ALL])
    private val seats: MutableList<Seat> = mutableListOf(),

    @Enumerated(EnumType.STRING)
    val status: ReservationStatus? = null,

) {

    init {
        user!!.addReservation(this)
        address!!.addReservation(this)
        payment!!.addReservation(this)
    }

    fun addSeat(seat: Seat) {
        seats.add(seat)
    }
}
