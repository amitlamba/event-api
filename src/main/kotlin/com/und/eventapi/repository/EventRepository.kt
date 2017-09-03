package com.und.eventapi.repository

import com.und.eventapi.model.Event
import org.springframework.data.mongodb.repository.MongoRepository

interface EventRepository : MongoRepository<Event, String> {

    fun findByName(eventName: String): List<Event>

}



