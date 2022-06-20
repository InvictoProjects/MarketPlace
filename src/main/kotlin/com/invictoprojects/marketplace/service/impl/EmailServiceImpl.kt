package com.invictoprojects.marketplace.service.impl

import com.invictoprojects.marketplace.service.EmailService
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

@Service
class EmailServiceImpl(private val mailSender: JavaMailSender) : EmailService {

    override fun sendEmail(sender: String, personal: String, receiver: String, subject: String, hyperText: String) {
        val message = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message)
        helper.apply {
            setFrom(sender, personal)
            setTo(receiver)
            setSubject(subject)
            setText(hyperText, true)
        }

        mailSender.send(message)
    }

}
