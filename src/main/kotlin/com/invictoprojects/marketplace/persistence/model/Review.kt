package com.invictoprojects.marketplace.persistence.model

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table
@IdClass(ReviewId::class)
class Review(
    @Id @ManyToOne var author: User,
    @Id @ManyToOne var product: Product,
    var rating: Int? = null,
    var date: Date,
    var content: String = ""
)
