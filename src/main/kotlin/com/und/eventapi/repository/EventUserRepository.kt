package com.und.eventapi.repository

import com.und.model.mongo.EventUser
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface EventUserRepository : MongoRepository<EventUser, String> {


    override fun findById(id: String): Optional<EventUser>

}