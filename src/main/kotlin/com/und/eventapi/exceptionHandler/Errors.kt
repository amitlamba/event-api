package com.und.eventapi.exceptionHandler

//FIXME create better heirarchy of classes to report error messages
//FIXME handle for list of error messages

class Errors(var errorCount: Int, var fieldName: String, var message: String)