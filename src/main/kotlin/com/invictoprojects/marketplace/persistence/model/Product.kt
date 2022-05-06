package com.invictoprojects.marketplace.persistence.model

import java.math.BigDecimal
import javax.persistence.*

@Entity
@Table
class Product(
    var name: String,
    var description: String? = null,
    var imagePath: String? = null,
    @ManyToOne var category: Category,
    @ManyToOne var seller: User,
    var price: BigDecimal,
    @Id @GeneratedValue var id: Long? = null
)
