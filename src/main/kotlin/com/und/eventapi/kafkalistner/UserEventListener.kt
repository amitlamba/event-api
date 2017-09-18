package com.und.eventapi.kafkalistner

import com.und.eventapi.model.Event
import com.und.eventapi.model.EventUser
import com.und.eventapi.service.EventService
import com.und.eventapi.service.EventUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.annotation.TopicPartition
import org.springframework.messaging.handler.annotation.Payload

class UserEventListener {

    //TODO externalize topic partition names

    @Value("\${kafka.ip}")
    lateinit private var ip: String

    @Value("\${kafka.topic.userevent}")
    lateinit private var topic: String

    @Autowired
    lateinit private var eventUserService: EventUserService
/*
, @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) key:Int,
                         @Header(KafkaHeaders.RECEIVED_PARTITION_ID) partition :Int,
                         @Header(KafkaHeaders.RECEIVED_TOPIC) topic :String,
                         @Header(KafkaHeaders.OFFSET) offset : Long
 */
    @KafkaListener(id = "id4", topicPartitions = arrayOf(TopicPartition(topic = "User-Event-API", partitions = arrayOf("0"))), containerFactory = "kafkaListenerContainerFactoryUser")
    fun listenPartition0(@Payload eventUser: EventUser) {
        //TODO handle mongo stuff, update eventUser details instead of save, matching instanceId
        //search for this in postgres
        //if found get undUserId, otherwise create new row
        //update undUserID in user data in mongo
        //update events with this instanceId and update undUserId
        eventUserService.save(eventUser)
    }

    @KafkaListener(id = "id5", topicPartitions = arrayOf(TopicPartition(topic = "User-Event-API", partitions = arrayOf("1"))), containerFactory = "kafkaListenerContainerFactoryUser")
    fun listenPartition1(@Payload eventUser: EventUser) {
        eventUserService.save(eventUser)
    }

    @KafkaListener(id = "id6", topicPartitions = arrayOf(TopicPartition(topic = "User-Event-API", partitions = arrayOf("2"))), containerFactory = "kafkaListenerContainerFactoryUser")
    fun listenPartition2(@Payload eventUser: EventUser) {
        eventUserService.save(eventUser)
    }
}