package com.invictoprojects.marketplace.persistence.model

import java.math.BigDecimal
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table
class Product(
    var name: String,
    var description: String? = null,
    var imagePath: String? = null,
    @ManyToOne var category: Category,
    @ManyToOne var seller: User? = null,
    var price: BigDecimal,
    var quantity: Long,
    var avgRating: Float? = null,
    var ratingCount: Long = 0,
    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER, cascade = [CascadeType.REMOVE], orphanRemoval = false)
    var reviews: MutableList<Review>? = null,
    @Id @GeneratedValue
    var id: Long? = null
)
