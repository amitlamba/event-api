package com.und.eventapi.model

import com.und.eventapi.validation.ValidateDate
import com.und.model.mongo.LineItem
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*
import javax.validation.constraints.*

/**
 * Created by shiv on 21/07/17.
 */

//FIXME handle validation for other fields and different types, e.g. @see
//@ValidateDate
open class Event {

    @NotBlank(message = "{event.name.empty}")
    lateinit var name: String

    @NotNull(message="{event.clientid.null}")
    var clientId: Int = -1

    var identity: Identity = Identity()
    var creationTime: Long = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()

    @Pattern(regexp="'\\b((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\\.|\$)){4}\\b'",
            message="{event.ip.invalid}")
    var ipAddress: String? = null

    @Size(min = 3, max = 25, message = "{event.city.invalidSize}")
    @Pattern(regexp = "^[A-Za-z][a-zA-Z\\s]*$",message = "{event.city.invalidCharacters}")
    var city: String? = null

    @Size(min = 4, max = 25, message = "{event.state.invalidSize}")
    @Pattern(regexp = "^[A-Za-z][a-zA-Z\\s]*$",message = "{event.state.invalidCharacters}")
    var state: String? = null

    @Size(min = 4, max = 25, message = "{event.country.invalidSize}")
    @Pattern(regexp = "^[A-Za-z][a-zA-Z\\s]*$",message = "{event.country.invalidCharacters}")
    var country: String? = null

    @Pattern(regexp = "^[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?),\\s*[-+]?(180(\\.0+)?|((1[0-7]\\d)|([1-9]?\\d))(\\.\\d+)?)\$",
            message="{event.latitude.invalid}")
    var latitude: String? = null

    @Pattern(regexp = "^[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?),\\s*[-+]?(180(\\.0+)?|((1[0-7]\\d)|([1-9]?\\d))(\\.\\d+)?)\$",
            message="{event.longitude.invalid}")
    var longitude: String? = null

    var agentString: String? = null
    var userIdentified: Boolean = false
    var lineItem: MutableList<LineItem> = mutableListOf()
    var attributes: HashMap<String, Any> = hashMapOf()

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    var startDate= LocalDate.now()

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    var endDate=LocalDate.of(2017, 1, 13)

}

data class Identity(
        //unique id assigned to a device, should always remain fixed, create new if not found
        var deviceId: String = "",
        //if userId is not found assign a new session id, handle change if user login changes, logouts etc
        var sessionId: String = "",
        // id of event user, this id is assigned when a user profile is identified.
        var userId: String? = null,
        var clientId: Int? = -1
)

class date : Event() {
    //TODO
}

/* {
    var eventUser: EventUser = EventUser()
}
*/





