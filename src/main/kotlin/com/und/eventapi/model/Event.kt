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

//TODO fix tenant provide as user login will not be available after kafka
@Document(collection = "#{tenantProvider.getTenant()}_event")
//@Document(collection = "event")
class Event {

    @Id
    private val id: String? = null

    var clientId: String? = null

    /**
     * userid to track
     *
     * @return userid
     */
    @get:ApiModelProperty(required = true, value = "userid to track")
    @get:NotNull
    var userid: Long? = null

    /**
     * if this property is true user details are tracked only for this session. all other user details are ignored.
     *
     * @return isanonymus
     */
    @get:ApiModelProperty(value = "if this property is true user details are tracked only for this session. all other user details are ignored. ")
    var isanonymus: Boolean? = null

    /**
     * Get username
     *
     * @return username
     */
    @get:ApiModelProperty(value = "")
    var username: String? = null

    /**
     * Get firstName
     *
     * @return firstName
     */
    @get:ApiModelProperty(value = "")
    var firstName: String? = null

    /**
     * Get lastName
     *
     * @return lastName
     */
    @get:ApiModelProperty(value = "")
    var lastName: String? = null

    /**
     * Get email
     *
     * @return email
     */
    @get:ApiModelProperty(value = "")
    var email: String? = null

    /**
     * Get phone
     *
     * @return phone
     */
    @get:ApiModelProperty(value = "")
    var phone: String? = null

    /**
     * Get time
     *
     * @return time
     */
    @get:ApiModelProperty(value = "")
    var time: String? = null


    @get:ApiModelProperty(required = true, value = "unique event name")
    @get:NotNull
    var name: String? = null

    @get:ApiModelProperty(value = "")
    var attributes: HashMap<String, Any>? = null

    constructor() {}

    constructor(name: String) {
        this.name = name
    }

    fun isanonymus(isanonymus: Boolean?): Event {
        this.isanonymus = isanonymus
        return this
    }

    fun username(username: String): Event {
        this.username = username
        return this
    }

    fun firstName(firstName: String): Event {
        this.firstName = firstName
        return this
    }

    fun lastName(lastName: String): Event {
        this.lastName = lastName
        return this
    }

    fun email(email: String): Event {
        this.email = email
        return this
    }

    fun phone(phone: String): Event {
        this.phone = phone
        return this
    }

    fun time(time: String): Event {
        this.time = time
        return this
    }

    override fun toString(): String {
        return "Event(id=$id, clientId=$clientId, userid=$userid, isanonymus=$isanonymus, username=$username, firstName=$firstName, lastName=$lastName, email=$email, phone=$phone, time=$time, name=$name, attributes=$attributes)"
    }


}
