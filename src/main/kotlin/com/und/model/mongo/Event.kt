package com.und.model.mongo

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.HashMap

@Document(collection = "#{tenantProvider.getTenant()}_event")
class Event(
        @field: Id var id: String? = "",
        val name: String,
        val clientId: Int,
        var lineItem: MutableList<LineItem> = mutableListOf(),
        var attributes: HashMap<String, Any> = hashMapOf(),
        var system: System? = null,
        val creationTime: Long = LocalDateTime.now().atZone(ZoneId.of("UTC")).toEpochSecond()
) {
    var geoLocation =  GeoDetails()
    var deviceId : String = ""
    var userIdentified :Boolean =  false
    var userId : String = ""
    var sessionId : String = ""
}

data class Coordinate(val latitude: Float, val longitude: Float)
data class GeoLocation(val type: String = "Point", val coordinate: Coordinate)
class GeoDetails {
    var ip: String? = null
    var geolocation: GeoLocation? = null
}

class SystemDetails(val name: String, val version: String)
class System {
    var os: SystemDetails? = null
    var browser: SystemDetails? = null
    var device: SystemDetails? = null
    var application: SystemDetails? = null
}


class LineItem {
    var price: Int = 0
    var currency: String? = null
    var product: String? = null
    var categories: MutableList<String> = mutableListOf()
    var tags: MutableList<String> = mutableListOf()
    var quantity:Int=0
    var properties: HashMap<String, Any> = hashMapOf()
}