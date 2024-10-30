package com.example.bookYourSeat.user.mail.service

interface MailService {
    fun sendCertMail(email: String): Boolean

    fun checkCertCode(email: String, certCode: String): Boolean

    fun checkVerifiedEmail(email: String)
}
