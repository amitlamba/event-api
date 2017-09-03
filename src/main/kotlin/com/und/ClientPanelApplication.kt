package com.und


import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class EventApplication

fun main(args: Array<String>) {

    SpringApplication.run(EventApplication::class.java, *args)
}