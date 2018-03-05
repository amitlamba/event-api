package com.und.service.eventapi

import com.und.messaging.eventapi.EventStream
import com.und.web.model.eventapi.Event
import com.und.web.model.eventapi.Identity
import com.und.repository.eventapi.EventRepository
import com.und.repository.eventapi.EventUserRepository
import com.und.eventapi.utils.copyToMongo
import com.und.security.utils.TenantProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.stream.annotation.StreamListener
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Service
import com.und.model.mongo.eventapi.Event as MongoEvent

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
        //FIXME add to metadata
        eventRepository.insert(mongoEvent)
    }

    fun updateEventWithUser(identity: Identity) {
        tenantProvider.setTenat(identity.clientId.toString())
        eventRepository.updateEventsWithIdentityMatching( identity)

    }


}