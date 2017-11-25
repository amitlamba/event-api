package com.und.eventapi.service

import com.und.eventapi.kafkalistner.EventStream
import com.und.eventapi.model.EventUser
import com.und.eventapi.model.Identity
import com.und.eventapi.repository.EventUserRepository
import com.und.security.utils.TenantProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.stream.annotation.StreamListener
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Service
import java.util.*

@Service
class EventUserService {

    @Autowired
    lateinit var tenantProvider: TenantProvider

    @Autowired
    lateinit private var eventUserRepository: EventUserRepository

    @Autowired
    lateinit private var eventStream: EventStream


    fun save(eventUser: EventUser): EventUser {
        val clientId = eventUser.clientId
        tenantProvider.setTenat(clientId!!)
        return eventUserRepository.save(eventUser)
    }

    @StreamListener("eventUser")
    @SendTo("processedEventUserProfile")
    fun processIdentity(identity: Identity): Identity {
        fun copyChangedValues(userId :String): EventUser {

            val existingEventUser = eventUserRepository.findById(userId)
            return if (existingEventUser.isPresent) {
                val existingUser = existingEventUser.get()
                val copyUser  = existingUser.copyNonNull(identity.eventUser)
                copyUser.id = userId
                copyUser.clientId = existingUser.clientId
                copyUser.creationDate = existingUser.creationDate
                copyUser

            } else {
                identity.eventUser
            }
        }

        val userId = identity.userId
        //FIXME throw some error message in case userid is found as null
        //userid will never be null here, this check is present only to satisfy compiler
        val newIdentity = identity.copy()
        newIdentity.eventUser = copyChangedValues(userId!!)
        save(newIdentity.eventUser )
        return newIdentity
    }


    @StreamListener("processedEventUserProfile")
    fun processedEventUserProfile(identity: Identity) {
        println(identity)
        //update all events where session id, machine id matches and userid is absent
        //save(identity.eventUser )
    }

    fun toKafka(identity: Identity): Boolean =
            eventStream.outputEventUser().send(MessageBuilder.withPayload(identity).build())

    /**
     * assign device id if absent
     * assign anonymous session id if absent
     * returns a copy of identity, doesnt change passed argument
     */
    fun initialiseIdentity(identity: Identity?): Identity {
        val identityCopy = identity?.copy() ?: Identity()

        with(identityCopy) {
            deviceId = deviceId ?: UUID.randomUUID().toString()
            sessionId = sessionId ?: UUID.randomUUID().toString()
        }
        //TODO verify old data exists if device id, session id is not null
        return identityCopy
    }

    fun logout(identity: Identity?): Identity {
        val identityCopy = identity?.copy() ?: Identity()

        with(identityCopy) {
            deviceId = deviceId ?: UUID.randomUUID().toString()
            sessionId = sessionId ?: UUID.randomUUID().toString()
        }
        //verify old data exists if device id, session id is not null
        return identityCopy
    }

    fun login(identity: Identity?): Identity {
        val identityCopy = identity?.copy() ?: Identity()

        with(identityCopy) {
            deviceId = deviceId ?: UUID.randomUUID().toString()
            sessionId = sessionId ?: UUID.randomUUID().toString()
        }
        //verify old data exists if device id, session id is not null
        return identityCopy
    }
}