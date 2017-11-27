package com.und.eventapi.rest

import com.und.eventapi.model.Event
import com.und.eventapi.model.Identity
import com.und.eventapi.service.EventService
import com.und.eventapi.service.EventUserService
import com.und.eventapi.utils.ipAddr
import com.und.security.model.Data
import com.und.security.model.Response
import com.und.security.model.ResponseStatus
import com.und.security.utils.TenantProvider
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@RestController
class EventRestController {

    @Autowired
    lateinit private var eventService: EventService

    @Autowired
    lateinit private var eventUserService: EventUserService

    @Autowired
    lateinit private var tenantProvider: TenantProvider

    @RequestMapping(value = "/event/initialize", produces = arrayOf("application/json"), consumes = arrayOf("application/json"), method = arrayOf(RequestMethod.POST))
    fun initialize(@Valid @RequestBody identity: Identity?): ResponseEntity<Response<Identity>> {

        return ResponseEntity.ok(Response(
                status = ResponseStatus.SUCCESS,
                data = Data(eventUserService.initialiseIdentity(identity))
        ))
    }

    @RequestMapping(value = "/push/event", produces = arrayOf("application/json"), consumes = arrayOf("application/json"), method = arrayOf(RequestMethod.POST))
    fun saveEvent(@Valid @RequestBody event: Event, request: HttpServletRequest): ResponseEntity<Response<String>> {
        val toEvent = buildEvent(event, request)
        eventService.toKafka(toEvent)
        return ResponseEntity.ok(Response(status = ResponseStatus.SUCCESS))
    }

    private fun buildEvent(fromEvent: Event, request: HttpServletRequest): Event {
        with(fromEvent) {

            clientId = tenantProvider.tenant
            geoDetails.ipAddress = request.ipAddr()
            systemDetails.agentString = request.getHeader("User-Agent")
        }
        return fromEvent
    }


    @RequestMapping(value = "/push/profile", produces = arrayOf("application/json"), consumes = arrayOf("application/json"), method = arrayOf(RequestMethod.POST))
    fun profile(@Valid @RequestBody identity: Identity): ResponseEntity<Response<Identity>> {

        //this method can't be called before identity has been initialized
        val identityInit = eventUserService.initialiseIdentity(identity)
        identityInit.userId = identityInit.userId ?: ObjectId.get().toString()

        val eventUser = identity.eventUser
        eventUser.id = identityInit.userId
        eventUser.clientId = tenantProvider.tenant
        identityInit.eventUser = eventUser
        eventUserService.toKafka(identityInit)
        //don't send event back rather send instance id, and status, also send a new instance id if user id changes
        return ResponseEntity.ok(Response(
                status = ResponseStatus.SUCCESS,
                data = Data(identityInit)
        ))
    }


    @RequestMapping(value = "/event/{name}", produces = arrayOf("application/json"), method = arrayOf(RequestMethod.GET))
    fun getEvent(@PathVariable("name") name: String): ResponseEntity<Response<List<Event>>> {
        val events = eventService.findByName(name)
        return ResponseEntity.ok(Response(
                status = ResponseStatus.SUCCESS,
                data = Data(events)
        ))
    }


}
