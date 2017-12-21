package com.und.eventapi.repository

import com.und.eventapi.model.Event
import com.und.eventapi.model.Identity

interface EventRepositoryUpdate {

    fun findEventsMatchingIdentity(identity:Identity):List<Event>

    fun updateEventsWithIdentityMatching(events: List<Event>,identity:Identity)
}