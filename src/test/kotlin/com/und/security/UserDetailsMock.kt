package com.und.security

import com.google.common.collect.Lists
import com.und.security.UndUserDetails
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.Date

/**
 * Created by shiv on 21/07/17.
 */
class UserDetailsMock : UndUserDetails {


    constructor(username: String) : super(
            1L,
            username,
            "firstname",
            "lastname",
            "email",
            "password",
            Lists.newArrayList(),
            true,
            Date(),
            "secret",
            "key"
    )



    override fun getAuthorities(): Collection<GrantedAuthority>? {
        return null
    }

    override fun getPassword(): String? {
        return null
    }


    override fun isAccountNonExpired(): Boolean {
        return false
    }

    override fun isAccountNonLocked(): Boolean {
        return false
    }

    override fun isCredentialsNonExpired(): Boolean {
        return false
    }

    override fun isEnabled(): Boolean {
        return false
    }
}
