package com.und.security

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority


class RestAuthenticationToken : UsernamePasswordAuthenticationToken {

    var key: String

    constructor(principal: UndUserDetails, credentials: String,
                authorities: Collection<GrantedAuthority>, key: String) : super(principal, credentials, authorities) {
        this.key = key
    }

}
