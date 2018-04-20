package com.und.eventapi.utils

import com.und.web.model.eventapi.Event
import com.und.web.model.eventapi.EventUser
import com.und.model.mongo.*
import com.und.model.mongo.eventapi.*
import com.und.model.mongo.eventapi.SystemDetails
import com.und.model.mongo.eventapi.Event as MongoEvent

fun Event.copyToMongo(): MongoEvent {
    val event = this
    val mongoEvent = MongoEvent(clientId = event.clientId, name = event.name)

    //copying system info
    val agentString = event.agentString
    if (agentString != null) {
        val sysDetail = systemDetails(agentString)
        val system = System()
        mongoEvent.system = system
        with(system) {

            os = SystemDetails(name = sysDetail.OS ?: "", version = "")
            if (sysDetail.browser != null && sysDetail.browserVersion != null) {
                browser = SystemDetails(sysDetail.browser!!, sysDetail.browserVersion!!)
            }
            application = SystemDetails(name = "", version = "")
            device = SystemDetails(name = sysDetail.deviceType ?: "", version = "")
        }

        with(mongoEvent.geogrophy) {
            val country = event.country
            val state = event.state
            val city = event.city
            mongoEvent.geogrophy=Geogrophy(country,state,city)
        }

    }

    //TODO fix null values or empty strings not allowed
    mongoEvent.userId = event.identity.userId
    mongoEvent.sessionId = event.identity.sessionId
    mongoEvent.deviceId = event.identity.deviceId

    //copy geo details
    with(mongoEvent.geoDetails) {
        ip = event.ipAddress
        //FIXME find a way to update coordinates
        val latitude = if (event.latitude != null) event.latitude?.toFloat() else 0.0f
        val longitude = if (event.longitude != null) event.longitude?.toFloat() else 0.0f
        if ((latitude != null && longitude != null) && (latitude != 0.0f && longitude != 0.0f)) {
        geolocation = GeoLocation(coordinate = Coordinate(latitude = latitude, longitude = longitude))
    }

    }
    //FIXME hard coded charged
    if ("charged".equals(event.name , ignoreCase = false)) {
        mongoEvent.lineItem = event.lineItem
    }
    //copy attributes
    mongoEvent.attributes.putAll(event.attributes)
    return mongoEvent
}

fun com.und.model.mongo.eventapi.EventUser.copyNonNull(eventUser: EventUser): com.und.model.mongo.eventapi.EventUser {
    fun unchanged(new: String?, old: String?): String? = when {
        new == old -> old
        old == null -> new
        new == null -> old
        else -> new
    }

    val copyEventUser = com.und.model.mongo.eventapi.EventUser()
    copyEventUser.id = id
    copyEventUser.additionalInfo.putAll(additionalInfo)
    copyEventUser.additionalInfo.putAll(eventUser.additionalInfo)
    copyEventUser.clientId = clientId
    copyEventUser.creationTime = creationTime

    copyEventUser.identity = Identity()
    copyEventUser.identity.clientUserId = unchanged(eventUser.clientUserId, identity.clientUserId)
    copyEventUser.identity.fbId = unchanged(eventUser.fbId, identity.fbId)
    copyEventUser.identity.googleId = unchanged(eventUser.googleId, identity.googleId)
    copyEventUser.identity.mobile = unchanged(eventUser.mobile, identity.mobile)
    copyEventUser.identity.email = unchanged(eventUser.email, identity.email)

    copyEventUser.standardInfo = StandardInfo()
    copyEventUser.standardInfo.firstname = unchanged(eventUser.firstName, standardInfo.firstname)
    copyEventUser.standardInfo.lastname = unchanged(eventUser.lastName, standardInfo.lastname)
    copyEventUser.standardInfo.gender = unchanged(eventUser.gender, standardInfo.gender)
    copyEventUser.standardInfo.dob = unchanged(eventUser.dob, standardInfo.dob)
    copyEventUser.standardInfo.country = unchanged(eventUser.country, standardInfo.country)
    //copyEventUser.standardInfo.countryCode = unchanged(eventUser.countryCode, standardInfo.countryCode)

    return copyEventUser
}