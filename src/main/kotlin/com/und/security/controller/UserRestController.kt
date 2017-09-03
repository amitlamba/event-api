package com.und.security.controller

import com.und.security.RestTokenUtil
import com.und.security.UndUserDetails
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

import javax.servlet.http.HttpServletRequest

@RestController
class UserRestController {

    @Value("\${security.header.token}")
    lateinit private var tokenHeader: String

    @Value("\${security.header.username}")
    lateinit private var usernameHeader: String

    @Autowired
    lateinit private var restTokenUtil: RestTokenUtil

    @Autowired
    lateinit private var userDetailsService: UserDetailsService

    @RequestMapping(value = "user", method = arrayOf(RequestMethod.GET))
    fun getAuthenticatedUser(request: HttpServletRequest): UndUserDetails {
        //val token = request.getHeader(tokenHeader)
        val username = request.getHeader(usernameHeader)
        val user = userDetailsService.loadUserByUsername(username) as UndUserDetails
        return user
    }

}
