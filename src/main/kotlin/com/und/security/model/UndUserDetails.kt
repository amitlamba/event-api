package com.und.security.model

import java.util.Date

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.userdetails.User

/**
 * Created by shiv on 21/07/17.
 */
class UndUserDetails(
        @get:JsonIgnore
        val id: Long?,

        private val username: String,

        val firstname: String?="",

        val lastname: String?="",

        private val password: String?=null,

        val email: String?=null,

        private val authorities: Collection<GrantedAuthority> = arrayListOf<GrantedAuthority>(),

        private val enabled: Boolean = false,

        @get:JsonIgnore
        val lastPasswordResetDate: Date?=null,

        @get:JsonIgnore
        val secret: String,

        @get:JsonIgnore
        val key: String?=null
) : User(username,password,authorities) {

    override fun getUsername(): String {
        return username
    }

    @JsonIgnore
    override fun isAccountNonExpired(): Boolean {
        return true
    }

    @JsonIgnore
    override fun isAccountNonLocked(): Boolean {
        return true
    }

    @JsonIgnore
    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    @JsonIgnore
    override fun getPassword(): String? {
        return password
    }

    override fun getAuthorities(): Collection<GrantedAuthority>? {
        return authorities
    }

    override fun isEnabled(): Boolean {
        return enabled
    }
}
