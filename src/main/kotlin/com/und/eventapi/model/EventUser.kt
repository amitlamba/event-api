package com.und.eventapi.model

import org.springframework.data.annotation.Id
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

class EventUser {
    var identity: Identity =  Identity()
    var email: String? = null
    var clientUserId: String? = null //this is id of the user client has provided
    var undId: String? = null
    var fbId: String? = null
    var googleId: String? = null
    var mobile: String? = null
    var firstName: String? = null
    var lastName: String? = null
    var gender: String? = null
    var dob: String? = null
    var country: String? = null
    var countryCode: String? = null
    var clientId: Int = -1 //client id , user is associated with, this can come from collection
    var additionalInfo: HashMap<String, Any> = hashMapOf()

    //FIXME creation date can't keep changing
    var creationDate: Long = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()



}




