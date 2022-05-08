package com.invictoprojects.marketplace.persistence.repository

import com.invictoprojects.marketplace.persistence.model.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CrudRepository<User, Long>
