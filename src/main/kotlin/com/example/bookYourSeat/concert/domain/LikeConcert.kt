package com.example.book_your_seat.concert_kotlin.domain

import com.example.book_your_seat.user.domain.User
import jakarta.persistence.*
import lombok.AccessLevel
import lombok.Getter
import lombok.NoArgsConstructor

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class LikeConcert(
    @field:JoinColumn(name = "user_id") @field:ManyToOne(fetch = FetchType.LAZY) private val user: User,
    @field:JoinColumn(
        name = "concert_id"
    ) @field:ManyToOne(fetch = FetchType.LAZY) private val concert: Concert
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_concert_id")
    private var id: Long? = null

    init {
        user.addLikeConcert(this)
        concert.addLikeConcert(this)
    }
}
