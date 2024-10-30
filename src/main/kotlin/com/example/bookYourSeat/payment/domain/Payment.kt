package com.example.bookYourSeat.payment.domain

import com.example.bookYourSeat.reservation.domain.Reservation
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Payment(
    var totalPrice: Long,
    var expiryAt: LocalDateTime,
    var discountRate: String,

    @Enumerated(EnumType.STRING)
    var paymentStatus: PaymentStatus,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    var reservation: Reservation? = null // 기본값을 null로 설정
) {
    constructor() : this(0L, LocalDateTime.now(), "", PaymentStatus.FAILED)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    var id: Long? = null

    // 기본 생성자는 필요하지 않음, Kotlin의 기본 생성자와 초기화 기능을 사용
    fun addReservation(reservation: Reservation) {
        this.reservation = reservation
    }
}
