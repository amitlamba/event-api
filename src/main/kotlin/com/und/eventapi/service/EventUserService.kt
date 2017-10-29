package com.und.eventapi.service

import com.und.eventapi.model.EventUser
import com.und.eventapi.repository.EventUserRepository
import com.und.security.utils.TenantProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Service
import org.springframework.util.concurrent.ListenableFutureCallback
import java.util.*
import javax.swing.text.html.Option

@Service
class EventUserService {

    @Autowired
    lateinit var tenantProvider: TenantProvider

    @Autowired
    lateinit private var eventUserRepository: EventUserRepository

    @Autowired
    lateinit private var kafkaTemplate: KafkaTemplate<String, EventUser>

    @Value("\${kafka.topic.userevent}")
    lateinit private var topic: String


    fun save(eventUser: EventUser): EventUser {
        val clientId = eventUser.clientId
        tenantProvider.setTenat(clientId)
        return eventUserRepository.insert(eventUser)//, collectionName)
    }

    fun findById(instanceID: String?): Optional<EventUser> {
        return eventUserRepository.findById(instanceID.toString())
    }

    fun toKafka(event: EventUser): EventUser {

        val future = kafkaTemplate.send(topic, event.clientId, event)
        future.addCallback(object : ListenableFutureCallback<SendResult<String, EventUser>> {
            override fun onSuccess(result: SendResult<String, EventUser>) {
                println("Sent message: " + result)
            }

            override fun onFailure(ex: Throwable) {
                println("Failed to send message")
            }
        })
        return event
    }

    fun initialiseUser(instanceId: String?): String? {
        //TODO move this data through kafka and refactor as immutable
        //TODO handle when user changes with same instance id, or user changes to different id, or add additional id
        return when {
            instanceId.isNullOrEmpty() -> save(EventUser(clientId = tenantProvider.tenant)).id
            findById(instanceId.toString()).isPresent -> return save(EventUser(clientId = tenantProvider.tenant)).id
            else -> instanceId
        }
    }
}