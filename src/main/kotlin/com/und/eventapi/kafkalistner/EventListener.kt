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

class EventListener {

    //TODO externalize topic partition names

    @Value("\${kafka.ip}")
    lateinit private var ip: String

    @Value("\${kafka.topic.event}")
    lateinit private var topic: String

    @Autowired
    lateinit private var eventService: EventService

    @Autowired
    lateinit private var eventUserService: EventUserService


    /*
    , @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) key:Int,
                             @Header(KafkaHeaders.RECEIVED_PARTITION_ID) partition :Int,
                             @Header(KafkaHeaders.RECEIVED_TOPIC) topic :String,
                             @Header(KafkaHeaders.OFFSET) offset : Long
     */
    @KafkaListener(id = "id0", topicPartitions = arrayOf(TopicPartition(topic = "Event-API", partitions = arrayOf("0"))), containerFactory = "kafkaListenerContainerFactory")
    fun listenPartition0(@Payload event: Event) {
        handleEvent(event)
    }

    @KafkaListener(id = "id1", topicPartitions = arrayOf(TopicPartition(topic = "Event-API", partitions = arrayOf("1"))), containerFactory = "kafkaListenerContainerFactory")
    fun listenPartition1(event: Event) {
        handleEvent(event)
    }

    @KafkaListener(id = "id2", topicPartitions = arrayOf(TopicPartition(topic = "Event-API", partitions = arrayOf("2"))), containerFactory = "kafkaListenerContainerFactory")
    fun listenPartition2(event: Event) {
        handleEvent(event)
    }

    private fun handleEvent(event: Event) {
        if (event.eventUser.isIdentified()) {
            eventUserService.toKafka(event.eventUser)
        }
        eventService.save(event)
    }


}
