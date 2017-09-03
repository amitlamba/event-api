package com.und.security

import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder

object AuthenticationUtils {

    val isUserLoggedIn: Boolean
        get() {
            val securityContext = SecurityContextHolder.getContext() ?: return false

            val authentication = securityContext.authentication
            return if (authentication == null || !authentication.isAuthenticated || authentication is AnonymousAuthenticationToken) {
                false
            } else true
        }

    val principal: Any
        get() {
            val securityContext = SecurityContextHolder.getContext() ?: throw AccessDeniedException("User is not logged in to the system.")

            val authentication = securityContext.authentication ?: throw AccessDeniedException("User is not logged in to the system.")

            return authentication.principal
        }


    val name: String?
        get() {
            val principal = principal
            return (principal as? UndUserDetails)?.username
        }

    val id: String
        get() {
            val principal = principal
            return if (principal is UndUserDetails) {
                "" + principal.id!!
            } else "_"
        }


}