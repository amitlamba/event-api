package com.und.eventapi.rest

import com.und.eventapi.model.Event
import com.und.eventapi.model.EventUser
import com.und.eventapi.model.GeoDetails
import com.und.eventapi.model.Initializer
import com.und.eventapi.service.EventService
import com.und.security.utils.TenantProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.mobile.device.Device
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

import com.und.eventapi.utils.ipAddr
import com.und.eventapi.utils.undUserId

import javax.validation.Valid

@RestController
@Scope("request")
class EventRestController {

    @Autowired
    lateinit private var eventService: EventService

    @Autowired
    lateinit private var tenantProvider: TenantProvider

    @RequestMapping(value = "/event", produces = arrayOf("application/json"), consumes = arrayOf("application/json"), method = arrayOf(RequestMethod.POST))
    fun saveEvent(@Valid @RequestBody event: Event, request:HttpServletRequest): Event {
        event.clientId = tenantProvider.tenant
        event.geoDetails = GeoDetails(ipAddress = request.ipAddr());
        event.eventUser = EventUser(undUserId= request.undUserId())
        request.undUserId()

        return eventService.saveToKafkaEvent(event)
    }


    @RequestMapping(value = "/event/{name}", produces = arrayOf("application/json"), method = arrayOf(RequestMethod.GET))
    fun getEvent(@PathVariable("name") name: String): List<Event> {
        return eventService.findByName(name)

    }

    @RequestMapping(value = "/event/initialize" , produces = arrayOf("application/json"), consumes = arrayOf("application/json"), method = arrayOf(RequestMethod.POST))
    fun initialize(@Valid @RequestBody initializer : Initializer, device : Device) :Initializer {

        return initializer
    }
}
