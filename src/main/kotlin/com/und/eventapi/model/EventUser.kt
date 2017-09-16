package com.und.eventapi.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "#{tenantProvider.getTenant()}_eventUser")
data class EventUser(
        @Id
        var id: String? = null,
        var undUserId: String="", //this is permanent id of user , in und db
        var instanceId: String="", //this is used when a new cookie value for und is found
        var clientUserId: String="", //this is id that client system identifies user with
        var clientId: String="", //id of the client in our system
        var deviceId: ArrayList<String> =  arrayListOf(),
        var fbId: String ="",
        var GoogleId: String="",
        var mobile: String="",
        var email: String="",
        var firstname:String="",
        var lastname:String="",
        val localDateTime: Long = System.currentTimeMillis()
)