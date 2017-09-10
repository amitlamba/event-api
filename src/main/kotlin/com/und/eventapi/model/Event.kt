package com.und.eventapi.model

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Clock
import java.time.LocalDateTime
import java.time.LocalDateTime.*
import javax.validation.constraints.NotNull
import java.util.HashMap

/**
 * Created by shiv on 21/07/17.
 */

@Document(collection = "#{tenantProvider.getTenant()}_event")
data class Event(
        var name: String = ""
) {

    @Id
    private val id: String? = null

    var clientId: String = ""

    var eventUser: EventUser = EventUser()

    var geoDetails: GeoDetails = GeoDetails()

    var systemDetails: SystemDetails = SystemDetails()

    var localDateTime: Long = System.currentTimeMillis()

    var attributes: HashMap<String, Any> = hashMapOf()

}
