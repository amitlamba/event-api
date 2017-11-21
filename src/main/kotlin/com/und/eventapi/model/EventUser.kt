package com.und.eventapi.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.HashMap

@Document(collection = "#{tenantProvider.getTenant()}_eventUser")
class EventUser {
    @Id
    private var id: String? = null
    var clientId: String = "-1" //client id , user is associated with, this can come from collection
    var deviceId: String? = null
    var clientUserId: String? = null//this is id of the user client has provided
    var socialId: SocialId = SocialId()
    var creationDate: Long = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()
    var standardInfo: StandardInfo = StandardInfo()
    var additionalInfo: HashMap<String, Any> = hashMapOf()
    fun isIdentified() = clientUserId != null || socialId.notEmpty() || standardInfo.notEmpty() || !additionalInfo.isEmpty()

}

data class SocialId(
        var fbId: String? = null,
        var googleId: String? = null,
        var mobile: String? = null,
        var email: String? = null
) {
    fun notEmpty() = fbId != null || googleId != null || mobile != null || email != null
}

data class StandardInfo(
        var firstName: String? = null,
        var lastName: String? = null,
        var gender: String? = null,
        var dob: String? = null,
        var country: String? = null,
        var countryCode: String? = null
) {
    fun notEmpty() = firstName != null || lastName != null || gender != null || dob != null || country != null || countryCode != null
}