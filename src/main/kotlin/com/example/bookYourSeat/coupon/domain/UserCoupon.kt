package com.example.bookYourSeat.coupon.domain

import com.example.bookYourSeat.common.entity.BaseEntity
import com.example.bookYourSeat.user.domain.User
import jakarta.persistence.*

@Entity
class UserCoupon(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_coupon_id")
    var id: Long = 0L,

    private var isUsed: Boolean = false,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    val coupon: Coupon? = null

) : BaseEntity() {

    init {
        user?.addUserCoupon(this)
        coupon?.addUserCoupon(this)
    }

    fun setUsed() {
        isUsed = true
    }
}