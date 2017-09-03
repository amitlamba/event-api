package com.und.eventapi

import com.und.common.utils.DateUtils
import com.und.security.RestTokenUtil
import com.und.security.RestUserFactory
import com.und.security.model.Authority
import com.und.security.model.AuthorityName
import com.und.security.model.User
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Matchers.any
import org.mockito.Matchers.anyString
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
class UserRestControllerTest {

    private var mvc: MockMvc? = null

    @Autowired
    lateinit private var context: WebApplicationContext

    @MockBean
    lateinit private var restTokenUtil: RestTokenUtil

    @MockBean
    lateinit private var userDetailsService: UserDetailsService

    @Before
    fun setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply<DefaultMockMvcBuilder>(springSecurity())
                .build()
    }

    @Test
    @Throws(Exception::class)
    fun shouldGetUnauthorizedWithoutRole() {

        this.mvc!!.perform(get("/user"))
                .andExpect(status().isUnauthorized)
    }

    @Test
    @WithMockUser(roles = arrayOf("USER"))
    @Throws(Exception::class)
    fun getPersonsSuccessfullyWithUserRole() {



        val user = buildMockUser(AuthorityName.ROLE_ADMIN)

        val eventUser = RestUserFactory.create(user)

        `when`<String>(this.restTokenUtil.getUsernameFromToken(anyString(), anyString())).thenReturn(user.username)

        `when`<UserDetails>(this.userDetailsService.loadUserByUsername(anyString())).thenReturn(eventUser)

        this.mvc!!.perform(get("/user"))
                .andExpect(status().is2xxSuccessful)
    }

    private fun buildMockUser(authorityNames: AuthorityName): User {

        val user = User()
        user.username = "username"
        user.firstname = "firstname"
        user.authorities = buildAuthorities(authorityNames)
        user.enabled = java.lang.Boolean.TRUE
        user.password = ""
        user.lastname = ""
        user.clientSecret = "secret"
        user.email = ""
        user.key = ""
        user.lastPasswordResetDate = DateUtils().now()
        user.lastPasswordResetDate = Date(System.currentTimeMillis() + 1000 * 1000)
        return user
    }

    private fun buildAuthorities(authorityNames: AuthorityName): List<Authority>? {


        val authority = Authority()
        authority.id = 1L
        authority.name = authorityNames
        val authorities = Arrays.asList(authority)
        return authorities
    }

}

