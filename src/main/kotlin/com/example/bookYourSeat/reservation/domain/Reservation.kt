package com.example.bookYourSeat.reservation.domain

import Seat
import com.example.bookYourSeat.payment.domain.Payment
import com.example.bookYourSeat.user.domain.Address
import com.example.bookYourSeat.user.domain.User
import jakarta.persistence.*

@Entity
class Reservation(
    @field:Enumerated(EnumType.STRING) val status: ReservationStatus,
    user: User,
    address: Address,
    payment: Payment
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    var id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private val user: User = user

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private val address: Address = address

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private val payment: Payment = payment

    @OneToMany(mappedBy = "reservation", cascade = [CascadeType.ALL])
    private val seats: MutableList<Seat> = mutableListOf()

    init {
        user.addReservation(this)
        address.addReservation(this)
        payment.addReservation(this)
    }

    fun addSeat(seat: Seat) {
        seats.add(seat)
    }
}
