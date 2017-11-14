package com.und.eventapi.rest

import com.und.eventapi.model.*
import com.und.eventapi.service.EventService
import com.und.eventapi.service.EventUserService
import com.und.security.utils.TenantProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.mobile.device.Device
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

import com.und.eventapi.utils.ipAddr
import com.und.security.model.Data
import com.und.security.model.Response
import com.und.security.model.ResponseStatus
import org.springframework.http.ResponseEntity

import javax.validation.Valid

@RestController
@Scope("request")
class EventRestController {

    @Autowired
    lateinit private var eventService: EventService

    @Autowired
    lateinit private var eventUserService: EventUserService

    @Autowired
    lateinit private var tenantProvider: TenantProvider

    @RequestMapping(value = "/event", produces = arrayOf("application/json"), consumes = arrayOf("application/json"), method = arrayOf(RequestMethod.POST))
    fun saveEvent(@Valid @RequestBody event: Event, request: HttpServletRequest, device: Device): ResponseEntity<Response<String>> {
        val toEvent = buildEvent(event, request, device)
        eventService.toKafka(toEvent)
        //TODO don't send event back rather send instance id, and status, also send a new instance id if user id changes
        return ResponseEntity.ok(Response(status = ResponseStatus.SUCCESS))
    }

    private fun buildEvent(fromEvent: Event, request: HttpServletRequest, device: Device): Event {
        //val eventUser = fromEvent.eventUser.copy(clientId = tenantProvider.tenant)
        //val event = fromEvent.copy(clientId = tenantProvider.tenant, eventUser = eventUser)
        with(fromEvent) {
            eventUser.clientId = tenantProvider.tenant
            clientId = tenantProvider.tenant
            geoDetails.ipAddress = request.ipAddr()
            systemDetails.agentString = request.getHeader("User-Agent")
        }
        return fromEvent
    }


    @RequestMapping(value = "/event/{name}", produces = arrayOf("application/json"), method = arrayOf(RequestMethod.GET))
    fun getEvent(@PathVariable("name") name: String): ResponseEntity<Response<List<Event>>> {
        val events = eventService.findByName(name)
        return ResponseEntity.ok(Response(
                status = ResponseStatus.SUCCESS,
                data = Data(events)
        ))
    }


    @RequestMapping(value = "/event/initialize", produces = arrayOf("application/json"), consumes = arrayOf("application/json"), method = arrayOf(RequestMethod.POST))
    fun initialize(@Valid @RequestBody instanceID: String?, device: Device):ResponseEntity<Response<EventUser>> {

        val eventUser = eventUserService.initialiseUser(instanceID)
        return ResponseEntity.ok(Response(
                status = ResponseStatus.SUCCESS,
                data = Data(eventUser)
        ))
    }


}
