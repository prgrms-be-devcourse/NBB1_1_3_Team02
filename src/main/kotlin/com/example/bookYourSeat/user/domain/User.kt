package com.example.bookYourSeat.user.domain

import com.example.bookYourSeat.common.entity.BaseEntity
import com.example.bookYourSeat.coupon.domain.UserCoupon
import com.example.bookYourSeat.reservation.domain.Reservation
import com.example.bookYourSeat.review.domain.Review
import jakarta.persistence.*

@Entity
class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    val id: Long? = null,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
    val addressList: MutableList<Address> = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
    val userCoupons: MutableList<UserCoupon> = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
    val reviews: MutableList<Review> = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
    val reservations: MutableList<Reservation> = mutableListOf(),

    var nickname: String = "",
    var username: String = "",
    var email: String = "",
    var password: String = "",
    @Enumerated(EnumType.STRING)
    var userRole: UserRole = UserRole.USER
) : BaseEntity() {

    constructor(): this(null)

    fun setAddress(address: Address) {
        this.addressList.add(address)
    }

    fun addUserCoupon(userCoupon: UserCoupon) {
        this.userCoupons.add(userCoupon)
    }

    fun addReview(review: Review) {
        this.reviews.add(review)
    }

    fun addReservation(reservation: Reservation) {
        this.reservations.add(reservation)
    }

    fun changeRoleToAdmin() {
        this.userRole = UserRole.ADMIN
    }
}
