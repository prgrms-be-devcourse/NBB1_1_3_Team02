package com.example.bookYourSeat.user.domain

enum class UserRole(
    val roleName: String
) {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN")
}