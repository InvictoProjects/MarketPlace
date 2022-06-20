package com.invictoprojects.marketplace.jobs

import com.invictoprojects.marketplace.persistence.model.Product
import com.invictoprojects.marketplace.service.EmailService
import com.invictoprojects.marketplace.service.RecommendationService
import freemarker.template.Configuration
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.io.StringWriter

@Component
class EmailEngine(
    private val emailService: EmailService,
    private val recommendationService: RecommendationService,
    private val configuration: Configuration,
    @Value("\${marketplace.email.recommendation.sender}") private val sender: String,
    @Value("\${marketplace.email.recommendation.personal}") private val personal: String,
    @Value("\${marketplace.email.recommendation.subject}") private val subject: String
) {

    @Scheduled(cron = "* * * */7 * *")
    fun sendProductRecommendationEmails() = runBlocking {
        recommendationService.getAllUsersWithRecommendedProducts()
            .forEach { pair ->
                launch {
                    val user = pair.first
                    val recommendedProducts = pair.second
                    val productText = createProductText(recommendedProducts)
                    val stringWriter = StringWriter()
                    configuration.getTemplate("recommendation_email.ftlh").process(
                        mapOf("products" to productText),
                        stringWriter
                    )

                    val emailText = stringWriter.buffer.toString()
                    emailService.sendEmail(sender, personal, user.email, subject, emailText)
                }
            }
    }

    private fun createProductText(products: List<Product>): String {
        val productNames = StringBuilder()
        for (i in products.indices) {
            val product = products[i]
            productNames.append(product.name)
            if (i != products.size - 1) {
                productNames.append(", ")
            }
        }
        return productNames.trim().toString()
    }

}
