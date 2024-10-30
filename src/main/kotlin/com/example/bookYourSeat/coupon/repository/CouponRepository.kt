package com.example.bookYourSeat.coupon.repository

import com.example.bookYourSeat.coupon.domain.Coupon
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface CouponRepository : JpaRepository<Coupon, Long>, CouponRepositoryCustom {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Coupon c WHERE c.id = :couponId")
    fun findByIdWithPessimistic(@Param("couponId") couponId: Long): Optional<Coupon>
}