package com.und.eventapi.service

import com.und.eventapi.model.Event
import com.und.eventapi.repository.EventRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class EventService {

    @Autowired
    private val eventRepository: EventRepository? = null

    fun findByName(name: String): List<Event> {
        return eventRepository!!.findByName(name)
    }

    fun saveEvent(event: Event): Event {
        return eventRepository!!.save(event)
    }
}
