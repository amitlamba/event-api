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

        var errorList: List<Errors> = listOf()
        val bindingError = ex.bindingResult
        val fieldErrors = bindingError.fieldErrors
        for (fieldError in fieldErrors) {
            if (fieldError != null && fieldError.defaultMessage != null) {
                errorList = listOf(
                        Errors(errorCount = bindingError.fieldErrorCount,
                                fieldName = fieldError.field,
                                message = fieldError.defaultMessage)
                )
            }
        }
        return  ErrorList(errorList)

    }


}





