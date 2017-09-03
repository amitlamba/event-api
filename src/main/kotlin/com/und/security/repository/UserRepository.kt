package com.und.security.repository

import org.springframework.data.jpa.repository.JpaRepository
import com.und.security.model.User

/**
 * Created by shiv on 21/07/17.
 */
interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
}
