package com.und.eventapi.exceptionHandler

import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.ExceptionHandler

@RestControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    fun handleFieldErrors(ex: MethodArgumentNotValidException): ErrorList {

        var errorList:List<Errors>?=null
        val bindingError = ex.bindingResult
        val fieldErrors = bindingError.fieldErrors
        for (fieldError in fieldErrors)
            errorList=listOf(Errors(errorCount = bindingError.fieldErrorCount,fieldName = fieldError.field, message = fieldError.defaultMessage))
        val errorListHandle= ErrorList(errorList)
        return errorListHandle

    }






//    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
//    fun handleFieldErrors(ex: MethodArgumentNotValidException): ErrorDetails {
//
//        val bindingError = ex.bindingResult
//        val e1 = bindingError.fieldError
//        val errorDetails = ErrorDetails(HttpStatus.NOT_ACCEPTABLE.value(),
//                "${e1.field} field has this error ${e1.rejectedValue}",
//                "${ex.cause} value is the cause of error",
//                "${e1.defaultMessage}")
//        return errorDetails
//
//    }


}





