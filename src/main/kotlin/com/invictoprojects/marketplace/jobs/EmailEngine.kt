package com.invictoprojects.marketplace.jobs

import com.invictoprojects.marketplace.persistence.model.Product
import com.invictoprojects.marketplace.service.EmailService
import com.invictoprojects.marketplace.service.ProductService
import com.invictoprojects.marketplace.service.UserService
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class EmailEngine(
    private val emailService: EmailService,
    private val userService: UserService,
    private val productService: ProductService,
    @Value("\${marketplace.email.recommendation.sender}") private val sender: String,
    @Value("\${marketplace.email.recommendation.personal}") private val personal: String
) {

    @Scheduled(cron = "*/20 * * * * *")
    fun sendProductRecommendationEmails() {
        val products = productService.findAll()
            .toList()

        userService.findAll()
            .forEach { user ->
                val recommendedProducts = mutableListOf<Product>()
                for (i in 0..2) {
                    val randomIndex = (products.indices).random()
                    val randomProduct = products[randomIndex]
                    recommendedProducts.add(randomProduct)
                }

                val emailText = createRecommendationEmailText(recommendedProducts)
                emailService.sendEmail(sender, personal, user.email, subject, emailText)
            }
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
                "<p>This is random 5 products recommended for you: %s</p>\n" +
                "</body>\n" +
                "</html>"

        private fun createRecommendationEmailText(products: List<Product>): String {
            val productNames = StringBuilder()
            for (product in products) {
                productNames.append(product.name)
                    .append(", ")
            }
            return String.format(text, productNames.trim())
        }
    }

}
