package com.und.eventapi.service

import com.und.eventapi.kafkalistner.EventStream
import com.und.eventapi.model.Event
import com.und.eventapi.model.EventUser
import com.und.eventapi.repository.EventUserRepository
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
import java.util.*
import javax.swing.text.html.Option

@Service
class EventUserService {

    @Autowired
    lateinit var tenantProvider: TenantProvider

    @Autowired
    lateinit private var eventUserRepository: EventUserRepository

    @Autowired
    lateinit private var eventStream: EventStream


    fun save(eventUser: EventUser):EventUser {
        val clientId = eventUser.clientId
        tenantProvider.setTenat(clientId)
        return eventUserRepository.insert(eventUser)
    }

    @StreamListener("eventUser")
    fun pushToMongo(eventUser: EventUser) {
        save(eventUser)
    }

    fun findById(instanceID: String?): Optional<EventUser> = eventUserRepository.findById(instanceID.toString())


    fun toKafka(event: EventUser): Boolean = eventStream.outputEventUser().send(MessageBuilder.withPayload(event).build())


    fun initialiseUser(instanceId: String?):EventUser {
        //TODO move this data through kafka and refactor as immutable
        //TODO handle when user changes with same instance id, or user changes to different id, or add additional id
        if(!instanceId.isNullOrEmpty()) {
            val eventOption = findById(instanceId.toString())
            if(eventOption.isPresent) {
                return eventOption.get()
            }
        }
        val eventUser = EventUser()
        eventUser.clientId = tenantProvider.tenant
        return save(eventUser)
    }
}