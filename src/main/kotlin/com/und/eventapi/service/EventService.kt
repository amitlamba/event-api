package com.und.eventapi.service

import com.und.eventapi.model.Event
import com.und.eventapi.repository.EventRepository
import com.und.eventapi.utils.systemDetails
import com.und.security.utils.TenantProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Service
import org.springframework.util.concurrent.ListenableFutureCallback

@Service
class EventService {


    @Autowired
    lateinit var tenantProvider: TenantProvider

    @Value("\${kafka.ip}")
    lateinit private var ip: String

    @Value("\${kafka.topic.event}")
    lateinit private var topic: String

    @Autowired
    lateinit private var kafkaTemplate: KafkaTemplate<String, Event>

    @Autowired
    lateinit private var eventRepository: EventRepository

    fun findByName(name: String): List<Event> {
        return eventRepository.findByName(name)
    }

    fun toKafka(event: Event): Event {

        val future = kafkaTemplate.send(topic, event.clientId, event)
        future.addCallback(object : ListenableFutureCallback<SendResult<String, Event>> {
            override fun onSuccess(result: SendResult<String, Event>) {
                println("Sent message: " + result)
            }

            override fun onFailure(ex: Throwable) {
                println("Failed to send message")
            }
        })
        return event
    }

    fun save(event: Event): Event {
        val clientId = event.clientId
        tenantProvider.setTenat(clientId)
        val agentString = event.systemDetails.agentString
        if(agentString!=null) {
            event.systemDetails = systemDetails(agentString)
        }
        return eventRepository.insert(event)
    }
}
