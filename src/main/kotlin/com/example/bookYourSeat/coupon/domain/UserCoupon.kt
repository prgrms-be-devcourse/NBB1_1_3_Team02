package com.example.bookYourSeat.coupon.domain

import com.example.book_your_seat.common.entity.BaseEntity
import jakarta.persistence.*

@Entity
class UserCoupon(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    val coupon: Coupon
) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_coupon_id")
    var id: Long? = null
        protected set

    var isUsed: Boolean = false
        protected set

    init {
        user.addUserCoupon(this)
        coupon.addUserCoupon(this)
    }

    fun setUsed() {
        isUsed = true
    }
}