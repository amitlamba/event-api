package com.und.eventapi.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

/**
 * Created by shiv on 21/07/17.
 */

@Document(collection = "#{tenantProvider.getTenant()}_event")
class Event() {
    @Id
    lateinit private var id: String
    lateinit var name: String
    var clientId: String = "-1"
    var eventUser: EventUser = EventUser()
    var geoDetails: GeoDetails = GeoDetails()
    var systemDetails: SystemDetails = SystemDetails()
    var creationTime: Long = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()
    var attributes: HashMap<String, Any> = hashMapOf()
    var userIdentified: Boolean = false
}

data class GeoDetails(
        var ipAddress: String? = null,
        var city: String? = null,
        var state: String? = null,
        var country: String? = null,
        var latitude: String? = null,
        var longitude: String? = null
)


data class SystemDetails(
        var OS: String? = null,
        var browser: String? = null,
        var browserVersion: String? = null,
        var deviceType: String? = null, //mobile, tablet, laptop etc
        var appId: String? = null,
        var agentString: String? = null

)

