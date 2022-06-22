package com.invictoprojects.marketplace.persistence.model

import java.time.Instant
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "tokens")
class RefreshToken(
    var token: String,
    @Column(name = "created_date") var createdDate: Instant,

    @OneToOne(fetch = FetchType.LAZY)
    var user: User,

    @Id @GeneratedValue
    var id: Long? = null
)
