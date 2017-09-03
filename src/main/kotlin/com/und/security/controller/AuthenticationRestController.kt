package com.und.security.controller

import com.und.security.RestAuthenticationRequest
import com.und.security.RestTokenUtil
import com.und.security.UndUserDetails
import com.und.security.service.SecurityAuthenticationResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.mobile.device.Device
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.bind.annotation.*

import javax.servlet.http.HttpServletRequest

@RestController
class AuthenticationRestController {

    @Value("\${security.header.token}")
    lateinit private var tokenHeader: String

    @Value("\${security.header.username}")
    lateinit private var usernameHeader: String

    @Autowired
    lateinit private var authenticationManager: AuthenticationManager

    @Autowired
    lateinit private var restTokenUtil: RestTokenUtil

    @Autowired
    lateinit private var userDetailsService: UserDetailsService

    @RequestMapping(value = "\${security.route.authentication.path}", method = arrayOf(RequestMethod.POST))
    @Throws(AuthenticationException::class)
    fun createAuthenticationToken(@RequestBody authenticationRequest: RestAuthenticationRequest, device: Device): ResponseEntity<*> {

        // Perform the security
        val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                        authenticationRequest.username,
                        authenticationRequest.password
                )
        )
        SecurityContextHolder.getContext().authentication = authentication

        // Reload password post-security so we can generate token
        val user:UndUserDetails? = userDetailsService.loadUserByUsername(authenticationRequest.username) as UndUserDetails?
        val token = if(user==null) "" else restTokenUtil.generateToken(user, device)

        // Return the token
        return ResponseEntity.ok(SecurityAuthenticationResponse(token))
    }

    @RequestMapping(value = "\${security.route.authentication.refresh}", method = arrayOf(RequestMethod.GET))
    fun refreshAndGetAuthenticationToken(request: HttpServletRequest
            //, @RequestHeader(tokenHeader)
    ): ResponseEntity<*> {
        val token = request.getHeader(tokenHeader)
        val username = request.getHeader(usernameHeader)
        val user = userDetailsService.loadUserByUsername(username) as UndUserDetails
        //val usernameFromToken = restTokenUtil.getUsernameFromToken(token, user.secret)

        if (restTokenUtil.canTokenBeRefreshed(token, user.lastPasswordResetDate!!, user.secret)) {
            val refreshedToken = restTokenUtil.refreshToken(token, user.secret)
            return ResponseEntity.ok(SecurityAuthenticationResponse(refreshedToken))
        } else {
            return ResponseEntity.badRequest().body<Any>(null)
        }
    }

}
