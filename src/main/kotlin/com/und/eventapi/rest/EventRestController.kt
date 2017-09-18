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
    fun saveEvent(@Valid @RequestBody event: Event, request: HttpServletRequest, device: Device): Event {
        buildEvent(event, request, device)
        eventService.toKafka(event)
        //TODO don't send event back rather send instance id, and status, also send a new instance id if user id changes
        return event
    }

    private fun buildEvent(event: Event, request: HttpServletRequest, device: Device): String? {
        event.clientId = tenantProvider.tenant
        event.geoDetails.ipAddress = request.ipAddr();
        event.instanceId = eventUserService.initialiseUser(event.instanceId)
        //event.eventUser.undUserId = request.undUserId() ?: ""
        event.eventUser.clientId = event.clientId
        event.systemDetails.agentString = request.getHeader("User-Agent")
        return event.instanceId
    }


    @RequestMapping(value = "/event/{name}", produces = arrayOf("application/json"), method = arrayOf(RequestMethod.GET))
    fun getEvent(@PathVariable("name") name: String): List<Event> {
        return eventService.findByName(name)

    }

    @RequestMapping(value = "/event/initialize", produces = arrayOf("application/json"), consumes = arrayOf("application/json"), method = arrayOf(RequestMethod.POST))
    fun initialize(@Valid @RequestBody instanceID: String?, device: Device): String? {

        return eventUserService.initialiseUser(instanceID)
    }


}
