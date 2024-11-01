package com.example.bookYourSeat.payment.domain

import com.example.bookYourSeat.reservation.domain.Reservation
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Payment(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    var id: Long? = null,

    var totalPrice: Long = 0L,
    var expiryAt: LocalDateTime = LocalDateTime.now(),
    var discountRate: String = "0%",

    @Enumerated(EnumType.STRING)
    var paymentStatus: PaymentStatus = PaymentStatus.FAILED,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    var reservation: Reservation? = null
) {

    fun addReservation(reservation: Reservation) {
        this.reservation = reservation
    }
}
