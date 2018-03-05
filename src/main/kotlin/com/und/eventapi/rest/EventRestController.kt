package com.und.eventapi.rest

import com.und.eventapi.model.Event
import com.und.eventapi.model.EventUser
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

@CrossOrigin
@RestController
class EventRestController {

    @Autowired
    private lateinit  var eventService: EventService

    @Autowired
    private lateinit var eventUserService: EventUserService

    @Autowired
    private lateinit var tenantProvider: TenantProvider

    @PostMapping(value = ["/event/initialize"], produces = ["application/json"], consumes =["application/json"])
    fun initialize(@Valid @RequestBody identity: Identity?): ResponseEntity<Response<Identity>> {

        return ResponseEntity.ok(Response(
                status = ResponseStatus.SUCCESS,
                data = Data(eventUserService.initialiseIdentity(identity))
        ))
    }

    @PostMapping(value = ["/push/event"], produces = ["application/json"], consumes =["application/json"])
    fun saveEvent(@Valid @RequestBody event: Event, request: HttpServletRequest): ResponseEntity<Response<String>> {
        val toEvent = buildEvent(event, request)
        eventService.toKafka(toEvent)
        return ResponseEntity.ok(Response(status = ResponseStatus.SUCCESS))
    }

    private fun buildEvent(fromEvent: Event, request: HttpServletRequest): Event {
        with(fromEvent) {

            clientId = tenantProvider.tenant.toInt()
            ipAddress = request.ipAddr()
            agentString = request.getHeader("User-Agent")
        }
        return fromEvent
    }


    @PostMapping(value = ["/push/profile"], produces = ["application/json"], consumes =["application/json"])
    fun profile(@Valid @RequestBody eventUser : EventUser): ResponseEntity<Response<Identity>> {

        //this method can't be called before identity has been initialized
        eventUserService.toKafka(eventUser)
        val identityInit = eventUserService.initialiseIdentity(eventUser.identity)
        identityInit.userId = identityInit.userId ?: ObjectId.get().toString()

        identityInit.clientId = tenantProvider.tenant.toInt()
        //don't send event back rather send instance id, and status, also send a new instance id if user id changes
        return ResponseEntity.ok(Response(
                status = ResponseStatus.SUCCESS,
                data = Data(identityInit)
        ))
    }

/*
    @RequestMapping(value = "/event/{name}", produces =["application/json"], method = [RequestMethod.GET])
    fun getEvent(@PathVariable("name") name: String): ResponseEntity<Response<List<Event>>> {
        val events = eventService.findByName(name)
        return ResponseEntity.ok(Response(
                status = ResponseStatus.SUCCESS,
                data = Data(events)
        ))
    }*/


}
