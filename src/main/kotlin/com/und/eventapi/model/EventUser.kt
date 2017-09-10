package com.und.eventapi.model

data class EventUser(
        var undUserId: String="", //this is permanent id of user , in und db
        var instanceId: String="", //this is used when a new cookie value for und is found
        var clientUserId: String="",
        var clientId: String="",
        var deviceId: ArrayList<String> =  arrayListOf(),
        var fbId: String ="",
        var GoogleId: String="",
        var mobile: String="",
        var email: String="",
        var firstname:String="",
        var lastname:String=""

)