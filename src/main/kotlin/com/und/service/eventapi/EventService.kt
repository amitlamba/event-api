package com.und.service.eventapi

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
        event.attributes.forEach { key, value ->
            val property = metadata.properties.find { it.name == key } ?: Property()
            property.options.add(value)
            if(property.name == null) {
                property.name = key
                metadata.properties += property
            }
        }
        metadata.properties.forEach { property ->
            property.dataType = dataType(property.options)
            if(property.dataType in setOf(DataType.Date, DataType.Number) ) {
                property.options.clear()
            }
            property.regex = null
        }

        return metadata
    }

    private fun dataType(options: MutableSet<Any>): DataType {

        fun findDataType(value: Any?): DataType {
            return when (value) {
                is Number, is Int, is Float,  is Double -> DataType.Number
                is Date, is LocalDate, is LocalDateTime -> DataType.Date
                is Array<*> -> {
                    if (value.isNotEmpty()) findDataType(value[0]) else DataType.String
                }
                else -> DataType.String
            }
        }

        val dataTypes = mutableSetOf<DataType>()
        options.forEach {
            dataTypes.add(findDataType(it))
        }
        //if multiple data types are found default to string
        return if(dataTypes.size==1) dataTypes.first() else DataType.String
    }

    fun updateEventWithUser(identity: Identity) {
        tenantProvider.setTenat(identity.clientId.toString())
        eventRepository.updateEventsWithIdentityMatching(identity)

    }


}
