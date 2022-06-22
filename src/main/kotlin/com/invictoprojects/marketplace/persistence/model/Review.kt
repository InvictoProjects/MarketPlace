package com.invictoprojects.marketplace.persistence.model

import java.time.Instant
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table
@IdClass(ReviewId::class)
class Review(
    @Id @ManyToOne var author: User? = null,
    @Id @ManyToOne var product: Product? = null,
    var rating: Int? = null,
    var date: Instant? = null,
    var content: String
)
