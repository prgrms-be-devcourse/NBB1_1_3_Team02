package com.example.bookYourSeat.user.mail.repository.util

import com.example.bookYourSeat.user.UserConst.MAIL_CREATION_FAILED
import com.example.book_your_seat.user.mail.MailConst
import jakarta.mail.MessagingException
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.thymeleaf.context.Context
import org.thymeleaf.spring6.SpringTemplateEngine
import java.security.SecureRandom
import java.util.concurrent.CompletableFuture

@Component
open class MailUtil(
    private val templateEngine: SpringTemplateEngine,
    private val mailSender: JavaMailSender,
    @Value("\${spring.mail.username}")
    val from: String
) {

    @Async
    open fun sendMail(to: String): CompletableFuture<String> {
        val message = mailSender.createMimeMessage()

        try {
            val helper = MimeMessageHelper(message, true, MailConst.UTF8)
            helper.setTo(to)
            helper.setSubject(MailConst.MAIL_SUBJECT)
            helper.setFrom(from)

            //데이터 설정
            val certCode = generateCertCode()
            val context = Context()
            context.setVariable(MailConst.MAIL, to)
            context.setVariable(MailConst.CERTCODE, certCode)

            //html 매핑
            val htmlContent = templateEngine.process(MailConst.EMAIL_TEMPLATE, context)
            helper.setText(htmlContent, true)

            //메일 전송
            mailSender.send(message)
            return CompletableFuture.completedFuture(certCode)
        } catch (e: MessagingException) {
            throw IllegalStateException(MAIL_CREATION_FAILED)
        }
    }

    private fun generateCertCode(): String {
        val candidateChars = MailConst.NUMBER_AND_ALPHABET
        val certCodeLength = 6
        val random = SecureRandom()
        val certCode = StringBuilder(certCodeLength)

        for (i in 0 until certCodeLength) {
            val index = random.nextInt(candidateChars.length)
            certCode.append(candidateChars[index])
        }
        return certCode.toString()
    }
}
