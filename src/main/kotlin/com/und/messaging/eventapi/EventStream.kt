package com.und.messaging.eventapi

import org.springframework.cloud.stream.annotation.Output
import org.springframework.messaging.MessageChannel

interface EventStream {

    @Output("event")
    fun outputEvent(): MessageChannel

    @Output("eventUser")
    fun outputEventUser(): MessageChannel

    @Output("processedEventUserProfile")
    fun processedEventUserProfile(): MessageChannel


}