package com.und.security

import java.util.Date

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

import com.fasterxml.jackson.annotation.JsonIgnore

/**
 * Created by shiv on 21/07/17.
 */
open class UndUserDetails : UserDetails {

    @get:JsonIgnore
    val id: Long?
    private val username: String
    val firstname: String?
    val lastname: String?
    private val password: String?
    val email: String?
    private val authorities: Collection<GrantedAuthority>?
    private val enabled: Boolean
    @get:JsonIgnore
    val lastPasswordResetDate: Date?
    @get:JsonIgnore
    val secret: String
    @get:JsonIgnore
    val key: String?


    constructor(username: String) {
        this.id = 0L
        this.username = username
        this.firstname = null
        this.lastname = null
        this.email = null
        this.password = null
        this.authorities = null
        this.enabled = false
        this.lastPasswordResetDate = null
        this.secret = ""
        this.key = null

    }

    constructor(
            id: Long?,
            username: String,
            firstname: String?,
            lastname: String?,
            email: String?,
            password: String?,
            authorities: Collection<GrantedAuthority>,
            enabled: Boolean,
            lastPasswordResetDate: Date?,
            secret: String,
            key: String?
    ) {
        this.id = id
        this.username = username
        this.firstname = firstname
        this.lastname = lastname
        this.email = email
        this.password = password
        this.authorities = authorities
        this.enabled = enabled
        this.lastPasswordResetDate = lastPasswordResetDate
        this.secret = secret
        this.key = key

    }

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
