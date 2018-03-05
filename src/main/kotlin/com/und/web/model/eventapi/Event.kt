package com.und.web.model.eventapi

import com.und.model.mongo.eventapi.LineItem
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

/**
 * Created by shiv on 21/07/17.
 */

class Event {
    lateinit var name: String
    var clientId: Int = -1
    var identity: Identity = Identity()
    var creationTime: Long = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()
    var ipAddress: String? = null
    var city: String? = null
    var state: String? = null
    var country: String? = null
    var latitude: String? = null
    var longitude: String? = null
    var agentString: String? = null
    var userIdentified: Boolean = false
    var lineItem: MutableList<LineItem> = mutableListOf()
    var attributes: HashMap<String, Any> = hashMapOf()
}

data class Identity(
        //unique id assigned to a device, should always remain fixed, create new if not found
        var deviceId: String = "",
        //if userId is not found assign a new session id, handle change if user login changes, logouts etc
        var sessionId: String = "",
        // id of event user, this id is assigned when a user profile is identified.
        var userId: String? = null,
        var clientId:Int? = -1
)/* {
    var eventUser: EventUser = EventUser()
}
*/





