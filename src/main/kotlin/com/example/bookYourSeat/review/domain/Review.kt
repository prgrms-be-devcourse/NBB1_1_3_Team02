package com.example.bookYourSeat.review.domain

import com.example.bookYourSeat.concert.domain.Concert
import com.example.bookYourSeat.user.domain.User
import jakarta.persistence.*
import lombok.AccessLevel
import lombok.NoArgsConstructor

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class Review(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    val id: Long? = null,

    var content: String = "empty",
    var starCount: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id")
    var concert: Concert? = null,
) {
    init {
        user!!.addReview(this)
        concert!!.addReview(this)
    }
}