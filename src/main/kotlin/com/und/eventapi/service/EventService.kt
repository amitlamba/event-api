package com.und.eventapi.service

import com.und.eventapi.kafkalistner.EventStream
import com.und.eventapi.model.Event
import com.und.eventapi.model.EventUser
import com.und.eventapi.repository.EventRepository
import com.und.eventapi.utils.systemDetails
import com.und.security.utils.TenantProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.stream.annotation.StreamListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Service
import org.springframework.util.concurrent.ListenableFutureCallback

@Service
class EventService {


    @Autowired
    lateinit var tenantProvider: TenantProvider

    @Autowired
    lateinit private var eventRepository: EventRepository

    @Autowired
    lateinit private var eventStream: EventStream

    fun findByName(name: String): List<Event> = eventRepository.findByName(name)


    fun toKafka(event: Event): Boolean = eventStream.outputEvent().send(MessageBuilder.withPayload(event).build())


    @StreamListener("event")
    fun save(event: Event) {
        val clientId = event.clientId
        tenantProvider.setTenat(clientId)
        val agentString = event.systemDetails.agentString
        if (agentString != null) {
            event.systemDetails = systemDetails(agentString)
        }
        eventRepository.insert(event)
    }


}
