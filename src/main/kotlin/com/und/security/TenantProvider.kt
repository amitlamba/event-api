package com.und.security

import org.springframework.stereotype.Component

@Component
class TenantProvider {

    val tenant: String
        get() = if (AuthenticationUtils.isUserLoggedIn) AuthenticationUtils.id else ""
}
