package com.und.service.eventapi

import com.und.messaging.eventapi.EventStream
import com.und.web.model.eventapi.EventUser
import com.und.model.mongo.eventapi.EventUser as MongoEventUser
import com.und.web.model.eventapi.Identity
import com.und.repository.eventapi.EventUserRepository
import com.und.security.utils.TenantProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.stream.annotation.StreamListener
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Service
import com.und.eventapi.utils.copyNonNull
import java.util.*

@Service
class EventUserService {

    @Autowired
    lateinit var tenantProvider: TenantProvider

    @Autowired
    private lateinit var eventUserRepository: EventUserRepository

    @Autowired
    private lateinit var eventService: EventService

    @Autowired
    private lateinit var eventStream: EventStream


    fun save(eventUser: MongoEventUser): MongoEventUser {
        val clientId = eventUser.clientId
        tenantProvider.setTenat(clientId.toString())
        //FIXME save to user profile metadata
        return eventUserRepository.save(eventUser)
    }

    @StreamListener("eventUser")
    @SendTo("processedEventUserProfile")
    fun processIdentity(eventUser: EventUser): Identity {

        val identity = eventUser.identity
        fun copyChangedValues(userId: String): MongoEventUser {

            val existingEventUser = eventUserRepository.findById(userId)
            val existingUser = if (existingEventUser.isPresent) existingEventUser.get() else MongoEventUser()
            return existingUser.copyNonNull(eventUser)
        }

        val userId = identity.userId
        if(userId != null) {
            val eventUserCopied = copyChangedValues(userId)
            val persistedUser = save(eventUserCopied)
            return Identity(userId = persistedUser.id, deviceId = identity.deviceId, sessionId = identity.sessionId)
        }else{
            throw IllegalArgumentException("user id should have been preset found null")
        }
    }


    @StreamListener("processedEventUserProfile")
    fun processedEventUserProfile(identity: Identity) {

        //println(identity)
        eventService.updateEventWithUser(identity)
        //update all events where session id, machine id matches and userid is absent
        //eventUserRepository.
        //save(identity.eventUser )
    }

    fun toKafka(eventUser: EventUser): Boolean =
            eventStream.outputEventUser().send(MessageBuilder.withPayload(eventUser).build())

    /**
     * assign device id if absent
     * assign anonymous session id if absent
     * returns a copy of identity, doesn't change passed argument
     */
    fun initialiseIdentity(identity: Identity?): Identity {
        val identityCopy = identity?.copy() ?: Identity()

        with(identityCopy) {
            deviceId = if (deviceId.isNullOrEmpty()) UUID.randomUUID().toString() else deviceId
            sessionId = if (sessionId.isNullOrEmpty()) UUID.randomUUID().toString() else sessionId
        }
        //TODO verify old data exists if device id, session id is not null
        return identityCopy
    }


    fun logout(identity: Identity?): Identity {
        val identityCopy = identity?.copy() ?: Identity()

        with(identityCopy) {
            deviceId = if (deviceId.isNullOrEmpty()) UUID.randomUUID().toString() else deviceId
            sessionId = if (sessionId.isNullOrEmpty()) UUID.randomUUID().toString() else sessionId
        }
        //verify old data exists if device id, session id is not null
        return identityCopy
    }

    fun login(identity: Identity?): Identity {
        val identityCopy = identity?.copy() ?: Identity()

        with(identityCopy) {
            deviceId = if (deviceId.isNullOrEmpty()) UUID.randomUUID().toString() else deviceId
            sessionId = if (sessionId.isNullOrEmpty()) UUID.randomUUID().toString() else sessionId
        }
        //verify old data exists if device id, session id is not null
        return identityCopy
    }
}