package com.und.model.mongo.eventapi

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.validation.constraints.Pattern

@Document(collection = "#{tenantProvider.getTenant()}_event")
class Event(
        @field: Id var id: String? = null,
        val name: String,
        val clientId: Int,
        var lineItem: MutableList<LineItem> = mutableListOf(),
        var attributes: HashMap<String, Any> = hashMapOf(),
        var system: System = System(),
        var creationTime: Long = LocalDateTime.now().atZone(ZoneId.of("UTC")).toEpochSecond()
) {
    var geoDetails = GeoDetails()
    var deviceId: String = ""
    var userIdentified: Boolean = false
    var userId: String? = null
    var sessionId: String = ""
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

    @Pattern(regexp = "^\\d+$",message = "{event.lineItem.price.invalid}")
    var price: Int = 0

    //FIXME currency validator logic
    //@ValidateCurrency("{event.lineItem.currency.invalid}")
    var currency: String? = null

    var product: String? = null

    var categories: MutableList<String> = mutableListOf()

    var tags: MutableList<String> = mutableListOf()

    @Pattern(regexp = "^\\d+$",message = "{event.lineItem.quantity.invalid}")
    var quantity: Int = 0

    var properties: HashMap<String, Any> = hashMapOf()
}
