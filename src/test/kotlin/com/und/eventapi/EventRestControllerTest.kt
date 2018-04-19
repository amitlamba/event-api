package com.und.eventapi

import io.restassured.RestAssured
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@RunWith(SpringRunner::class)
@SpringBootTest
class EventRestControllerTest {

    private var mvc: MockMvc? = null

    @Autowired
    private val context: WebApplicationContext? = null

    @Before
    fun setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context!!)
                .apply<DefaultMockMvcBuilder>(SecurityMockMvcConfigurers.springSecurity())
                .build()
    }

    @Test
    @Throws(Exception::class)
    fun shouldGetUnauthorizedWithoutRole() {

        this.mvc!!.perform(MockMvcRequestBuilders.post("/event"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }

    //TODO fix this failing test case
    @Test
    @WithMockUser(roles = arrayOf("USER"))
    @Throws(Exception::class)
    fun getPersonsSuccessfullyWithUserRole() {

        this.mvc!!.perform(MockMvcRequestBuilders.get("/event/test"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
    }



}
