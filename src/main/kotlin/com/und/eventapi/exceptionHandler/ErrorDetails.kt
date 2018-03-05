package com.und.eventapi.exceptionHandler

class ErrorDetails(val status: Int,
                   val error: String,
                   val cause: String,
                   val message: String
)