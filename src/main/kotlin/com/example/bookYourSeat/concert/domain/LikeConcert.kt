package com.example.bookYourSeat.concert.domain

import com.example.bookYourSeat.user.domain.User
import jakarta.persistence.*
import lombok.AccessLevel
import lombok.Getter
import lombok.NoArgsConstructor

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class LikeConcert(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_concert_id")
    private var id: Long = 0L,

    @JoinColumn(name = "user_id") @field:ManyToOne(fetch = FetchType.LAZY)
    private val user: User? = null,

    @JoinColumn(name = "concert_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private val concert: Concert? = null
) {

}
