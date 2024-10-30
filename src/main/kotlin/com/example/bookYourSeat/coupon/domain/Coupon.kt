package com.example.bookYourSeat.coupon.domain

import jakarta.persistence.*
import java.time.LocalDate

@Entity
class Coupon(
    var amount: Int,

    @Enumerated(EnumType.STRING)
    val discountRate: DiscountRate,

    val expirationDate: LocalDate
) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    var id: Long? = null
        protected set

    @OneToMany(mappedBy = "coupon", cascade = [CascadeType.ALL], orphanRemoval = true)
    val userCoupons: MutableList<UserCoupon> = mutableListOf()


    fun addUserCoupon(userCoupon: UserCoupon) {
        userCoupons.add(userCoupon)
    }

    fun issue() {
        require(amount > 0) { "수량이 부족합니다." }
        amount -= 1
    }
}