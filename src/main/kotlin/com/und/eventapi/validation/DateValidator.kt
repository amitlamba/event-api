package com.und.eventapi.validation


import java.time.LocalDate
import javax.validation.ConstraintValidatorContext
import javax.validation.ConstraintValidator
import java.time.format.DateTimeFormatter

class DateValidator : ConstraintValidator<ValidateDate, LocalDate> {

    var annotation: ValidateDate?=null
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override fun initialize(annotation: ValidateDate) {
        this.annotation = annotation
    }

    override fun isValid(value: LocalDate?, context: ConstraintValidatorContext?): Boolean {

        val startDate: LocalDate = LocalDate.parse(annotation!!.startDate, dateFormatter)
        val endDate: LocalDate = LocalDate.parse(annotation!!.endDate, dateFormatter)
        return startDate < endDate
    }
}
