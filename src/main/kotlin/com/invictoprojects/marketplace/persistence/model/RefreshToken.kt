package com.invictoprojects.marketplace.persistence.model

import java.time.Instant
import javax.persistence.*

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
