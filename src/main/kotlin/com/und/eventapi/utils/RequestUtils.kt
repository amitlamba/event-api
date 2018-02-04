package com.und.eventapi.utils

import com.und.eventapi.model.Event
import com.und.eventapi.model.EventUser
import com.und.model.mongo.EventUser as MongoEventUser
import com.und.model.mongo.Event as MongoEvent
import com.und.model.mongo.Identity
import com.und.model.mongo.StandardInfo
import com.und.model.mongo.System
import eu.bitwalker.useragentutils.UserAgent
import javax.servlet.http.HttpServletRequest

//TODO write test cases for this class

fun HttpServletRequest.ipAddr(): String {


    fun ipExistsInHeader(header: String): Boolean {
        val ip = this.getHeader(header)
        return !(ip.isNullOrEmpty() || "unknown".equals(ip, ignoreCase = true))
    }

    val ip = when {
        ipExistsInHeader("X-Forwarded-For") -> this.getHeader("X-Forwarded-For")
        ipExistsInHeader("Proxy-Client-IP") -> this.getHeader("Proxy-Client-IP")
        ipExistsInHeader("WL-Proxy-Client-IP") -> this.getHeader("WL-Proxy-Client-IP")
        ipExistsInHeader("HTTP_CLIENT_IP") -> this.getHeader("HTTP_CLIENT_IP")
        ipExistsInHeader("HTTP_X_FORWARDED_FOR") -> this.getHeader("HTTP_X_FORWARDED_FOR")
        else -> this.remoteAddr
    }
    return ip
}


fun systemDetails(agentString: String): SystemDetails {
    val systemDetails = SystemDetails()
    val userAgent = UserAgent.parseUserAgentString(agentString)
    val browser = userAgent.browser

    val operatingSystem = userAgent.operatingSystem
    val deviceType = operatingSystem.deviceType
    val osName = operatingSystem.getName()
    systemDetails.browser = browser.getName()
    systemDetails.browserVersion = userAgent.browserVersion.version
    systemDetails.OS = osName
    systemDetails.deviceType = deviceType.getName()

    return systemDetails
}

data class SystemDetails(
        var OS: String? = null,
        var browser: String? = null,
        var browserVersion: String? = null,
        var deviceType: String? = null, //mobile, tablet, laptop etc
        var agentString: String? = null
)

//FIXME move this method to appropriate place
fun MongoEventUser.copyNonNull(eventUser: EventUser): MongoEventUser {
    fun unchanged(new: String?, old: String?): String? = when {
        new == old -> old
        old == null -> new
        new == null -> old
        else -> new
    }

    val copyEventUser = MongoEventUser()
    copyEventUser.id = id
    copyEventUser.additionalInfo.putAll(additionalInfo)
    copyEventUser.additionalInfo.putAll(eventUser.additionalInfo)
    copyEventUser.clientId = clientId
    copyEventUser.creationTime = creationTime

    copyEventUser.identity = Identity()
    copyEventUser.identity?.clientUserId = unchanged(eventUser.clientUserId, identity?.clientUserId)
    copyEventUser.identity?.fbId = unchanged(eventUser.fbId, identity?.fbId)
    copyEventUser.identity?.googleId = unchanged(eventUser.googleId, identity?.googleId)
    copyEventUser.identity?.mobile = unchanged(eventUser.mobile, identity?.mobile)
    copyEventUser.identity?.email = unchanged(eventUser.email, identity?.email)

    copyEventUser.standardInfo = StandardInfo()
    copyEventUser.standardInfo?.firstname = unchanged(eventUser.firstName, standardInfo?.firstname)
    copyEventUser.standardInfo?.lastname = unchanged(eventUser.lastName, standardInfo?.lastname)
    copyEventUser.standardInfo?.gender = unchanged(eventUser.gender, standardInfo?.gender)
    copyEventUser.standardInfo?.dob = unchanged(eventUser.dob, standardInfo?.dob)
    copyEventUser.standardInfo?.country = unchanged(eventUser.country, standardInfo?.country)
    //copyEventUser.standardInfo?.countryCode = unchanged(eventUser.countryCode, standardInfo?.countryCode)

    return copyEventUser
}

fun MongoEvent.copy(event: Event): MongoEvent {
    val mongoEvent = MongoEvent(clientId = event.clientId, name = event.name)

    //copying system info
    val agentString = event.agentString
    if (agentString != null) {
        val sysDetail = systemDetails(agentString)
        val system = System()
        mongoEvent.system = system
        with(system) {

            os = com.und.model.mongo.SystemDetails(name = "", version = "")
            if (sysDetail.browser != null && sysDetail.browserVersion != null) {
                browser = com.und.model.mongo.SystemDetails(sysDetail.browser!!, sysDetail.browserVersion!!)
            }
            application = com.und.model.mongo.SystemDetails(name = "", version = "")
            device = com.und.model.mongo.SystemDetails(name = "", version = "")
        }
    }

    //TODO copy user info
    //copy geodetails
    //copy line items if charged event
    //copy attributes
    return mongoEvent
}

