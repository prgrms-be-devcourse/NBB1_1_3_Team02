package com.example.book_your_seat.review.domain

import com.example.book_your_seat.concert.domain.Concert
import com.example.book_your_seat.user.domain.User
import jakarta.persistence.*
import lombok.AccessLevel
import lombok.NoArgsConstructor

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class Review(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    val id: Long? = null,

    var content: String,
    var starCount: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id")
    var concert: Concert
) {
    init {
        user.addReview(this)
        concert.addReview(this)
    }
}