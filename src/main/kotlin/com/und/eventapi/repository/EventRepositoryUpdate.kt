package com.und.eventapi.repository

import com.und.model.mongo.Event
import com.und.eventapi.model.Identity

interface EventRepositoryUpdate {

    fun findEventsMatchingIdentity(identity:Identity):List<Event>

    fun updateEventsWithIdentityMatching(identity:Identity)
}