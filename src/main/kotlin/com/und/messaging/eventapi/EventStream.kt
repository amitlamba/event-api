package com.und.messaging.eventapi

import org.springframework.cloud.stream.annotation.Input
import org.springframework.messaging.MessageChannel


interface EventStream {

    @Input("event")
    fun outputEvent(): MessageChannel

    @Input("eventUser")
    fun outputEventUser(): MessageChannel

    @Input("processedEventUserProfile")
    fun processedEventUserProfile(): MessageChannel


/*    @Output("event")
    fun readEvent(): SubscribableChannel

    @Output("eventUser")
    fun readEventUser(): SubscribableChannel

    @Output("processedEventUserProfile")
    fun readEventUserProfile(): SubscribableChannel*/



}