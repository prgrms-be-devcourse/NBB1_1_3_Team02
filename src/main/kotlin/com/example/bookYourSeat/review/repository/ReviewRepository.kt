package com.example.bookYourSeat.review.repository

import com.example.bookYourSeat.review.domain.Review
import org.springframework.data.jpa.repository.JpaRepository

interface ReviewRepository : JpaRepository<Review, Long>