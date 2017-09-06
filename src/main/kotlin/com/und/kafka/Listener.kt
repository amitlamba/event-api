package com.und.kafka

import com.und.eventapi.model.Event
import com.und.eventapi.service.EventService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.annotation.TopicPartition
import java.util.concurrent.CountDownLatch

class Listener {

    //TODO externalize topic partition names

    @Value("\${kafka.ip}")
    lateinit private var ip: String

    @Value("\${kafka.topic}")
    lateinit private var topic: String

    @Autowired
    lateinit private var eventService: EventService

    @KafkaListener(id = "id0", topicPartitions = arrayOf(TopicPartition(topic = "Event-API", partitions = arrayOf("0"))))
    fun listenPartition0(event: Event) {
        println("Listener Id0, Thread ID: " + Thread.currentThread().id)
        println("Received: " + event)
        eventService.saveToMongoEvent(event)
    }

    @KafkaListener(id = "id1", topicPartitions = arrayOf(TopicPartition(topic = "Event-API", partitions = arrayOf("1"))))
    fun listenPartition1(event: Event) {
        println("Listener Id1, Thread ID: " + Thread.currentThread().id)
        println("Received: " + event)
        eventService.saveToMongoEvent(event)
    }

    @KafkaListener(id = "id2", topicPartitions = arrayOf(TopicPartition(topic = "Event-API", partitions = arrayOf("2"))))
    fun listenPartition2(event: Event) {
        println("Listener Id2, Thread ID: " + Thread.currentThread().id)
        println("Received: " + event)
        eventService.saveToMongoEvent(event)
    }
}
