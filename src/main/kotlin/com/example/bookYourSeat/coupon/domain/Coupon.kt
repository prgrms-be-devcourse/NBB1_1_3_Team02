package com.example.bookYourSeat.coupon.domain

import com.example.bookYourSeat.common.entity.BaseEntity
import jakarta.persistence.*
import java.time.LocalDate

@Entity
class Coupon(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    var id: Long? = null,

    @OneToMany(mappedBy = "coupon", cascade = [CascadeType.ALL], orphanRemoval = true)
    var userCoupons: MutableList<UserCoupon> = mutableListOf(),

    var amount: Int = 0,

    @Enumerated(EnumType.STRING)
    val discountRate: DiscountRate = DiscountRate.FIVE,

    val expirationDate: LocalDate = LocalDate.now(),
) : BaseEntity() {

    fun addUserCoupon(userCoupon: UserCoupon) {
        userCoupons.add(userCoupon)
    }

    fun issue() {
        require(amount > 0) { "수량이 부족합니다." }
        amount -= 1
    }
}