package com.und.service.eventapi

import com.und.common.utils.MetadataUtil
import com.und.eventapi.utils.copyToMongo
import com.und.messaging.eventapi.EventStream
import com.und.model.mongo.eventapi.DataType
import com.und.model.mongo.eventapi.EventMetadata
import com.und.model.mongo.eventapi.Property
import com.und.repository.eventapi.EventMetadataRepository
import com.und.repository.eventapi.EventRepository
import com.und.repository.eventapi.EventUserRepository
import com.und.security.utils.TenantProvider
import com.und.web.model.eventapi.Event
import com.und.web.model.eventapi.Identity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.stream.annotation.StreamListener
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import com.und.model.mongo.eventapi.Event as MongoEvent

@Service
class EventService {


    @Autowired
    private lateinit var tenantProvider: TenantProvider

    @Autowired
    private lateinit var eventRepository: EventRepository

    @Autowired
    private lateinit var eventMetadataRepository: EventMetadataRepository

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
        val eventMetadata = buildMetadata(mongoEvent)
        eventMetadataRepository.save(eventMetadata)
        //FIXME add to metadata
        eventRepository.insert(mongoEvent)
    }

    private fun buildMetadata(event: MongoEvent): EventMetadata {
        val metadata = eventMetadataRepository.findByName(event.name) ?: EventMetadata()
        metadata.name = event.name
        val properties = MetadataUtil.buildMetadata(event.attributes, metadata.properties)
        metadata.properties.clear()
        metadata.properties.addAll(properties)
        return metadata
    }



    fun updateEventWithUser(identity: Identity) {
        tenantProvider.setTenat(identity.clientId.toString())
        eventRepository.updateEventsWithIdentityMatching(identity)

    }


}
