package com.und.eventapi.utils

import com.und.eventapi.model.Event
import com.und.model.mongo.System
import com.und.model.mongo.SystemDetails
import com.und.model.mongo.Event as MongoEvent

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

            os = SystemDetails(name = sysDetail.OS?:"", version = "")
            if (sysDetail.browser != null && sysDetail.browserVersion != null) {
                browser = SystemDetails(sysDetail.browser!!, sysDetail.browserVersion!!)
            }
            application = SystemDetails(name = "", version = "")
            device = SystemDetails(name = sysDetail.deviceType?:"", version = "")
        }
    }

    //TODO fix null values or empty strings not allowed
    mongoEvent.userId = event.identity.userId
    mongoEvent.sessionId = event.identity.sessionId
    mongoEvent.deviceId = event.identity.deviceId

    //copy geodetails
    with(mongoEvent.geoDetails) {
        ip = event.ipAddress
        //FIXME find a way to update cordinates
        //geolocation = GeoLocation(coordinate = Coordinate(latitude = event.latitude, longitude = event.longitude))

    }
    //copy line items if charged event
    if(event.name == "charged") {
        mongoEvent.lineItem = event.lineItem
    }
    //copy attributes
    mongoEvent.attributes.putAll(event.attributes)
    return mongoEvent
}