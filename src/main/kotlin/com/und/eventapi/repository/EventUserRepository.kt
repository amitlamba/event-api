package com.und.eventapi.repository

import com.und.eventapi.model.Event
import com.und.eventapi.model.EventUser
import org.springframework.data.mongodb.repository.MongoRepository

interface EventUserRepository : MongoRepository<EventUser, String> {

    fun findById(id: String): List<EventUser>

}