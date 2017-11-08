package com.und.eventapi.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.util.*

/**
 * Created by shiv on 21/07/17.
 */

@Document(collection = "#{tenantProvider.getTenant()}_event")
data class Event(
        @Id
        private var id: String? = null,
        val name: String = "",
        var clientId: String = "-1",
        var instanceId: String? = null,
        val eventUser: EventUser = EventUser(),
        val geoDetails: GeoDetails = GeoDetails(),
        val systemDetails: SystemDetails = SystemDetails(),
        val localDateTime:LocalDateTime = LocalDateTime.now(),//System.currentTimeMillis(),
        val attributes: HashMap<String, Any> = hashMapOf(),
        var userIdentified:Boolean = false
)

