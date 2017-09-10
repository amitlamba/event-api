package com.und.eventapi.utils

import javax.servlet.http.HttpServletRequest

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


fun HttpServletRequest.undUserId():String {
    return this.cookies.filter { it.name=="und.uid" }.map { it.value }.joinToString { it.toString() }
}

