package com.und.model.mongo

import org.springframework.data.annotation.Id
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.HashMap

class EventUser(
        @field: Id var id: String = "",
        val clientId: Int,
        val identity: Identity,
        val standardInfo: StandardInfo,
        val additionalInfo: HashMap<String, Any> = hashMapOf(),
        val creationTime: LocalDateTime) {
}

class Identity {
    var email: String? = null
    var clientUserId: String? = null
    var undId: String? = null
    var fbid: String? = null
    var gid: String? = null
    var mobile: String? = null

}

data class CommunicationDetails(val id: String, var dnd: Boolean = false)
class Communication {
    var email: CommunicationDetails? = null
    var mobile: CommunicationDetails? = null
}

class StandardInfo {
    var gender: String? = null
    var dob: LocalDate? = null
    var languages: MutableList<String> = mutableListOf()
    var country: String? = null
    var City: String? = null
    var Address: String? = null
}