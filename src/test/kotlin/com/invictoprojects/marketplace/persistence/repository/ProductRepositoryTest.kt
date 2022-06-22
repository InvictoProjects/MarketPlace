package com.invictoprojects.marketplace.persistence.repository

import com.invictoprojects.marketplace.persistence.model.Category
import com.invictoprojects.marketplace.persistence.model.Product
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import java.math.BigDecimal

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    lateinit var entityManager: TestEntityManager

    @Autowired
    lateinit var productRepository: ProductRepository


    @Test
    fun whenFindByKeyword_thenReturnProducts() {
        val category = Category("category1")
        val product1 = Product("product1", "keyword1", null, category, null,
            BigDecimal(999.99), 10)
        val product2 = Product("product2", "keyword1, keyword2", null, category, null,
            BigDecimal(1999.99), 2)

        entityManager.persist(category)
        entityManager.persist(product1)
        entityManager.persist(product2)
        entityManager.flush()
        
        val products = productRepository.findByKeyword("keyword1")
        assertThat(products == listOf(product1, product2))
    }

    @Test
    fun whenFindByKeyword_thenReturnEmptyList() {
        val category = Category("category1")
        val product1 = Product("product1", "keyword1", null, category, null,
            BigDecimal(999.99), 10)
        val product2 = Product("product2", "keyword1, keyword2", null, category, null,
            BigDecimal(1999.99), 2)

        entityManager.persist(category)
        entityManager.persist(product1)
        entityManager.persist(product2)
        entityManager.flush()

        val products = productRepository.findByKeyword("keyword3")
        assertThat(products.isEmpty())
    }
}
