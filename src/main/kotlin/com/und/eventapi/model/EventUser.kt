package com.und.eventapi.model

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

class EventUser {
    var identity: Identity = Identity()

    @Email
    var email: String? = null
    var clientUserId: String? = null //this is id of the user client has provided
    var undId: String? = null
    var fbId: String? = null
    var googleId: String? = null

    //TODO Use custom validators here

    @Size(min=10,max=10,message = "{user.mobile.invalidSize}")
    @Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\d{10}\$",message = "{user.mobile.invalidDigits}")
    var mobile: String? = null

    @NotBlank(message = "{user.firstName.empty}")
    @Size(min = 5, max = 15, message = "{user.firstName.invalidSize}")
    @Pattern(regexp = "^[A-Za-z][a-zA-Z\\s]*$",message = "{user.firstName.invalidCharacters}")
    var firstName: String? = null

    @NotBlank(message = "{user.lastName.empty}")
    @Size(min = 5, max = 15, message = "{user.lastName.invalidSize}")
    @Pattern(regexp = "^[A-Za-z][a-zA-Z\\s]*$",message = "{user.lastName.invalidCharacters}")
    var lastName: String? = null

    var gender: String? = null

    @Pattern(regexp="((19|20)\\d\\d)/(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])",message = "{user.dob.invalid}")
    var dob: String? = null

    @Size(min = 4, max = 25, message = "{user.country.invalidSize}")
    @Pattern(regexp = "^[A-Za-z][a-zA-Z\\s]*$",message = "{user.country.invalidCharacters}")
    var country: String? = null

    var countryCode: String? = null
    var clientId: Int = -1 //client id , user is associated with, this can come from collection
    var additionalInfo: HashMap<String, Any> = hashMapOf()

    //FIXME creation date can't keep changing
    var creationDate: Long = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()



}




