package com.und.eventapi

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder

@RunWith(SpringRunner::class)
@SpringBootTest
class MethodProtectedRestControllerTest {

    private var mvc: MockMvc? = null

    @Autowired
    lateinit private var  context: WebApplicationContext

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
        this.mvc!!
                .perform(get("/protected"))
                .andExpect(status().isUnauthorized)
    }

    @Test
    @WithMockUser(roles = arrayOf("USER"))
    @Throws(Exception::class)
    fun shouldGetForbiddenWithUserRole() {
        this.mvc!!
                .perform(get("/protected"))
                .andExpect(status().isForbidden)
    }

    @Test
    @WithMockUser(roles = arrayOf("ADMIN"))
    @Throws(Exception::class)
    fun shouldGetOkWithAdminRole() {
        this.mvc!!
                .perform(get("/protected"))
                .andExpect(status().is2xxSuccessful)
    }

}

