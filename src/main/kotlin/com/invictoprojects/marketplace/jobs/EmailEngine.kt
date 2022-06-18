package com.invictoprojects.marketplace.jobs

import com.invictoprojects.marketplace.service.EmailService
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class EmailEngine(
    private val emailService: EmailService,
    @Value("\${marketplace.email.recommendation.sender}") private val sender: String,
    @Value("\${marketplace.email.recommendation.personal}") private val personal: String
) {

    @Scheduled(cron = "* * * * * *")
    fun sendProductRecommendationEmails() {
        emailService.sendEmail(sender, personal, sender, subject, text)
    }

    companion object {

        private const val subject = "MarketPlace - products that are recommended especially for you"
        private const val text = "<!doctype html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\n" +
                "    <title>Recommended products</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hi there,</p>\n" +
                "<p>This is test email</p>\n" +
                "</body>\n" +
                "</html>"
    }

}
