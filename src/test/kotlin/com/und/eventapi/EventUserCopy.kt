package com.und.eventapi

import com.und.eventapi.model.EventUser
import org.hamcrest.core.IsEqual
import org.junit.Assert
import org.junit.Test

class EventUserTest {

    @Test
    fun now() {
        val eventUserDb = EventUser()
        eventUserDb.clientUserId = "100"
        with(eventUserDb.standardInfo) {
            firstName = "hello"
            lastName = "mugabe"
            country = "Zimbabwe"
        }
        with(eventUserDb.socialId) {
            fbId = "boringboooker"
            googleId = "mugabe@zimbabwe.com"
            email = "mugabe@zimbabwe.com"
        }

        val eventUserNew = EventUser()
        eventUserNew.clientUserId = "200"
        with(eventUserNew.standardInfo) {
            firstName = "namaste"
            lastName = null
            countryCode = "he he"

        }
        with(eventUserNew.socialId) {
            fbId = "coolBooker"
            googleId = null
            email = "newmugambe@zimbabwe.com"
        }

        val copiedUser = eventUserDb.copyNonNull(eventUserNew)
        Assert.assertThat(copiedUser.clientUserId , IsEqual.equalTo("200"))
        Assert.assertThat(copiedUser.standardInfo.lastName , IsEqual.equalTo("mugabe"))
        Assert.assertThat(copiedUser.standardInfo.firstName , IsEqual.equalTo("namaste"))
        Assert.assertThat(copiedUser.standardInfo.country , IsEqual.equalTo("Zimbabwe"))
        Assert.assertThat(copiedUser.standardInfo.countryCode , IsEqual.equalTo("he he"))


        Assert.assertThat(copiedUser.socialId.fbId , IsEqual.equalTo("coolBooker"))
        Assert.assertThat(copiedUser.socialId.googleId , IsEqual.equalTo("mugabe@zimbabwe.com"))
        Assert.assertThat(copiedUser.socialId.email , IsEqual.equalTo("newmugambe@zimbabwe.com"))
    }
}