package com.und.security


import com.und.security.model.Authority
import com.und.security.model.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

class RestUserFactory {
    companion object {

        @JvmStatic
        fun create(user: User): UndUserDetails {
            return UndUserDetails(
                    user.id,
                    user.username,
                    user.firstname,
                    user.lastname,
                    user.email,
                    user.password,
                    mapToGrantedAuthorities(user.authorities),
                    user.enabled,
                    user.lastPasswordResetDate,
                    user.clientSecret,
                    user.key
            )
        }

        private fun mapToGrantedAuthorities(authorities: List<Authority>?): List<GrantedAuthority> {
            return authorities!!.map { authority -> SimpleGrantedAuthority(authority.name!!.name) }

        }
    }
}
