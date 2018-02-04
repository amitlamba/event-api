package com.und.eventapi.service

import com.und.eventapi.kafkalistner.EventStream
import com.und.eventapi.model.Event
import com.und.model.mongo.Event as MongoEvent
import com.und.eventapi.model.Identity
import com.und.eventapi.repository.EventRepository
import com.und.eventapi.repository.EventUserRepository
import com.und.eventapi.utils.systemDetails
import com.und.model.mongo.System
import com.und.model.mongo.SystemDetails
import com.und.security.utils.TenantProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.stream.annotation.StreamListener
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Service
import com.und.eventapi.utils.copy
import com.und.eventapi.utils.copyToMongo

@Service
class EventService {


    @Autowired
    private lateinit var tenantProvider: TenantProvider

    @Autowired
    private lateinit var eventRepository: EventRepository

    @Autowired
    private lateinit var eventUserRepository: EventUserRepository

    @Autowired
    private lateinit var eventStream: EventStream

    fun findByName(name: String): List<MongoEvent> = eventRepository.findByName(name)


    fun toKafka(event: Event): Boolean = eventStream.outputEvent().send(MessageBuilder.withPayload(event).build())


    @StreamListener("event")
    fun save(event: Event) {

        val clientId = event.clientId
        tenantProvider.setTenat(clientId.toString())
        val mongoEvent = event.copyToMongo()
        eventRepository.insert(mongoEvent)
    }

    fun updateEventWithUser(identity: Identity) {
        tenantProvider.setTenat(identity.clientId.toString())
        //val events: List<MongoEvent> = eventRepository.findEventsMatchingIdentity(identity)
        eventRepository.updateEventsWithIdentityMatching( identity)

    }


}
