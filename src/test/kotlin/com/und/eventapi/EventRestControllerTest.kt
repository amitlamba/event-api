package com.und.eventapi

import io.restassured.RestAssured
import org.json.JSONObject
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@RunWith(SpringRunner::class)
@SpringBootTest
class EventRestControllerTest {

    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var context: WebApplicationContext

    @Before
    fun setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply<DefaultMockMvcBuilder>(SecurityMockMvcConfigurers.springSecurity())
                .build()
    }

    @Test
    @Throws(Exception::class)
    fun shouldGetUnauthorizedWithoutRole() {

        this.mvc.perform(MockMvcRequestBuilders.post("/event"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }

    //TODO fix this failing test case
    @Test
    @WithMockUser(roles = ["USER"])
    @Throws(Exception::class)
    fun getPersonsSuccessfullyWithUserRole() {

        this.mvc!!.perform(MockMvcRequestBuilders.get("/event/test"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
    }

    @Test
    @Throws(Exception::class)
    fun pushEventWithoutAuthorization() {
        this.mvc.perform(MockMvcRequestBuilders.post("/event"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }


    @Test
    @Throws(Exception::class)
    fun eventInitializeWithoutLoginDetails() {
        this.mvc.perform(post("/event"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }

    @Test
    fun checkAuth() {

        val loginDetails = "{\"username\":\"kamalpreet.singh@userndot.com\",\"password\":\"Kamal123!\"}"
        val request = RestAssured.given()
        request.body(loginDetails)
        request.header("Content-Type", "application/json")
        val response = request.post("http://192.168.0.109:8080/auth/auth").thenReturn().body()
        //response.then().assertThat().statusCode(200)
        val jsonResponseBody = JSONObject(response.asString())
        val value=jsonResponseBody.getJSONObject("data")
        val token=value.getString("token")
        this.mvc.perform(post("/push/event").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk)
    }
}

