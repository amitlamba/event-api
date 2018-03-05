package com.und.eventapi.validation

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Constraint(validatedBy = [(DateValidator::class)])
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.FIELD, AnnotationTarget.ANNOTATION_CLASS,
        AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ValidateDate(val startDate: String, val endDate: String, val message: String = "{event.date.invalid}",
                              val groups: Array<KClass<*>> = [],
                              val payload: Array<KClass<out Payload>> = [])