package com.und.eventapi.model

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.annotations.ApiModelProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

import javax.validation.constraints.NotNull
import java.util.HashMap

/**
 * Created by shiv on 21/07/17.
 */

@Document(collection = "#{tenantProvider.getTenant()}_event")
class Event (
        @get:ApiModelProperty(required = true, value = "unique event name")
        @get:NotNull
        var name :String?
) {

    @Id
    private val id: String? = null

    var clientId: String? = null

    @get:ApiModelProperty(required = true, value = "one of the id of the user to be tracked")
    @get:NotNull
    var userid: Long? = null

    @get:ApiModelProperty(value = """if this property is true user details are tracked only for this session.
         all other user details are ignored. """)
    var isAnonymous: Boolean? = null

    @get:ApiModelProperty(value = "")
    var username: String? = null

    @get:ApiModelProperty(value = "")
    var firstName: String? = null

    @get:ApiModelProperty(value = "")
    var lastName: String? = null

    @get:ApiModelProperty(value = "")
    var email: String? = null

    @get:ApiModelProperty(value = "")
    var phone: String? = null

    @get:ApiModelProperty(value = "")
    var time: String? = null

    @get:ApiModelProperty(value = "")
    var attributes: HashMap<String, Any>? = null

    override fun toString(): String {
        return "Event(id=$id, clientId=$clientId, userid=$userid, isAnonymous=${isAnonymous}, username=$username, firstName=$firstName, lastName=$lastName, email=$email, phone=$phone, time=$time, name=$name, attributes=$attributes)"
    }


}
