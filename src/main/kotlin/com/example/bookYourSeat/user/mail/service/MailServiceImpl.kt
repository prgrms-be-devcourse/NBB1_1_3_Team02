package com.example.bookYourSeat.user.mail.service

import com.example.bookYourSeat.user.UserConst.EMAIL_NOT_VERIFIED
import com.example.bookYourSeat.user.UserConst.INVALID_CERT_CODE
import com.example.bookYourSeat.user.mail.repository.MailRedisRepository
import com.example.bookYourSeat.user.mail.repository.util.MailUtil
import com.example.book_your_seat.user.mail.MailConst
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class MailServiceImpl(
    private val mailUtil: MailUtil,
    private val mailRedisRepository: MailRedisRepository
) : MailService {

    private val log: Logger = LoggerFactory.getLogger(MailServiceImpl::class.java)


    override fun sendCertMail(email: String): Boolean {
        mailUtil.sendMail(email).thenAccept { certCode: String ->
            mailRedisRepository.saveEmailCertCode(email, certCode)
            log.info("{} {}", MailConst.MAIL_SUCCESS, email)
        }
        return true
    }

    override fun checkCertCode(email: String, certCode: String): Boolean {
        val savedCertCode = mailRedisRepository.findCertCodeByEmail(email)

        compareCertCode(certCode, savedCertCode)

        mailRedisRepository.saveVerifiedEmail(email)
        return true
    }

    override fun checkVerifiedEmail(email: String) {
        if (mailRedisRepository.findVerifiedEmail(email) == null) throw IllegalArgumentException(EMAIL_NOT_VERIFIED)
    }

    private fun compareCertCode(certCode: String, savedCertCode: String?) {
        if (savedCertCode != certCode) {
            throw IllegalArgumentException(INVALID_CERT_CODE)
        }
    }
}
