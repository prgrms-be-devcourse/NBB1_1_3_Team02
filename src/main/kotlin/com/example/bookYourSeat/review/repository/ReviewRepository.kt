package com.example.book_your_seat.review.repository

import com.example.book_your_seat.review.domain.Review
import org.springframework.data.jpa.repository.JpaRepository

interface ReviewRepository : JpaRepository<Review, Long>