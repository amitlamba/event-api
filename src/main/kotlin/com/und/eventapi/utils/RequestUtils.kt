package com.und.eventapi.utils

import com.und.eventapi.model.SystemDetails
import eu.bitwalker.useragentutils.UserAgent
import java.util.HashMap
import javax.servlet.http.HttpServletRequest
import eu.bitwalker.useragentutils.*

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


fun HttpServletRequest.undUserId(): String {
    return this.cookies.filter { it.name == "und.uid" }.map { it.value }.joinToString { it.toString() }
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
    systemDetails.deviceType = deviceType.name

    return systemDetails
}

