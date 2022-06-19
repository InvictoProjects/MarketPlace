package com.invictoprojects.marketplace.jobs

import com.invictoprojects.marketplace.persistence.model.Product
import com.invictoprojects.marketplace.service.EmailService
import com.invictoprojects.marketplace.service.ProductService
import com.invictoprojects.marketplace.service.UserService
import freemarker.template.Configuration
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.io.StringWriter

@Component
class EmailEngine(
    private val emailService: EmailService,
    private val userService: UserService,
    private val productService: ProductService,
    private val configuration: Configuration,
    @Value("\${marketplace.email.recommendation.sender}") private val sender: String,
    @Value("\${marketplace.email.recommendation.personal}") private val personal: String,
    @Value("marketplace.email.recommendation.subject") private val subject: String
) {

    @Scheduled(cron = "*/20 * * * * *")
    fun sendProductRecommendationEmails() {
        val products = productService.findAll()
            .toList()

        userService.findAll()
            .filter { user -> user.isSubscribed }
            .forEach { user ->
                val recommendedProducts = mutableListOf<Product>()
                for (i in 0..2) {
                    val randomIndex = (products.indices).random()
                    val randomProduct = products[randomIndex]
                    recommendedProducts.add(randomProduct)
                }

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
