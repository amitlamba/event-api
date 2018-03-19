package com.und.web.model.eventapi

import com.und.eventapi.validation.ValidateID
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*
import javax.validation.constraints.*

class EventUser {
    var identity: Identity = Identity()

    @Email
    var email: String? = null
    var clientUserId: String? = null //this is id of the user client has provided
    var undId: String? = null
    var fbId: String? = null
    var googleId: String? = null

    //TODO Use custom validators here

    @Size(min=10,max=10,message = "{eventUser.mobile.invalidSize}")
    @Pattern(regexp = "\\d",message = "{user.mobile.invalidDigits}")
    var mobile: String? = null

    @NotBlank(message = "{user.firstName.empty}")
    @Size(min = 3, max = 20, message = "{eventUser.firstName.invalidSize}")
    @Pattern(regexp = "[A-Za-z][a-zA-Z\\s]*",message = "{eventUser.firstName.invalid}")
    var firstName: String? = null

    @Size(min = 3, max = 15, message = "{eventUser.lastName.invalidSize}")
    @Pattern(regexp = "[A-Za-z][a-zA-Z\\s]*",message = "{eventUser.lastName.invalid}")
    var lastName: String? = null

    @Pattern(regexp = "[A-Za-z]+",message = "{eventUser.gender.invalidCharacters}")
    var gender: String? = null

    @Pattern(regexp="(\\d{4})[-](0?[1-9]|1[012])[-](0?[1-9]|[12][0-9]|3[01])",message = "{eventUser.dob.invalid}")
    var dob: String? = null

    @Size(min = 4, max = 35, message = "{eventUser.country.invalidSize}")
    @Pattern(regexp = "[A-Za-z][a-zA-Z\\s]*",message = "{eventUser.country.invalid}")
    var country: String? = null

    //TODO validate country code
    var countryCode: String? = null

    @ValidateID(message = "{event.clientId.invalid}")
    @Min(value=1,message="{event.clientId.zero}")
    var clientId: Int = -1 //client id , user is associated with, this can come from collection

    var additionalInfo: HashMap<String, Any> = hashMapOf()

    //FIXME creation date can't keep changing
    var creationDate: Long = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()



}




