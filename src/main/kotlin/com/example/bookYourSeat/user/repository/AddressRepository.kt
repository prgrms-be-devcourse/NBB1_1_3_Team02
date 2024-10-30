package com.example.bookYourSeat.user.repository

import com.example.bookYourSeat.user.domain.Address
import io.lettuce.core.dynamic.annotation.Param
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface AddressRepository : JpaRepository<Address, Long> {
    @Query("SELECT a FROM Address a JOIN FETCH a.user u WHERE a.id = :addressId")
    fun findByIdWithUser(@Param("addressId") addressId: Long): Address?
}