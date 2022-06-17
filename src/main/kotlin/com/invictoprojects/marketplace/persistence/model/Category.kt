package com.invictoprojects.marketplace.persistence.model

import javax.persistence.*

@Entity
@Table
class Category(

    @Column(unique = true)
    var name: String,

    @Id @GeneratedValue var id: Long? = null

) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Category

        if (name != other.name) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + (id?.hashCode() ?: 0)
        return result
    }

}
