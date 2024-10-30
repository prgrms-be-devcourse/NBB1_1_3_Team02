package com.example.bookYourSeat.user.repository

import com.example.bookYourSeat.user.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface UserRepository : JpaRepository<User, Long> {


    fun existsByEmail(Email: String): Boolean

    @Query("SELECT u FROM User u WHERE u.email = :email")
    fun findByEmail(@org.springframework.data.repository.query.Param("email") email: String): User?

    @Query("select distinct u from User u join fetch u.userCoupons uc join fetch uc.coupon where u.id = :userId")
    fun findByIdWithUserCoupons(@org.springframework.data.repository.query.Param("userId") userId: Long): User?
}