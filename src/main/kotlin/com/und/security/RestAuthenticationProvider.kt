package com.und.security

import com.und.security.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Component

/**
 * Created by shiv on 14/08/17.
 */
@Component
class RestAuthenticationProvider : AuthenticationProvider {

    @Autowired
    private val userRepository: UserRepository? = null

    @Autowired
    private val restTokenUtil: RestTokenUtil? = null

    @Throws(AuthenticationException::class)
    override fun authenticate(authentication: Authentication): Authentication {
        val token = authentication as RestAuthenticationToken
        val user = RestUserFactory.create(userRepository!!.findByUsername(token.getName())!!)

        if (user != null || token.key.equals(user.key)) {
            if (restTokenUtil!!.validateToken(user.key!!, user)) {
                return RestAuthenticationToken(user, user.password!!, user.authorities!!, token.key)
            }
        }


        throw BadCredentialsException("The credentials are invalid")

    }

    override fun supports(authentication: Class<*>): Boolean {
        return RestAuthenticationToken::class.java == authentication
    }

}