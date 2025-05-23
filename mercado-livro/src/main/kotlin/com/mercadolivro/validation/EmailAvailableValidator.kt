package com.mercadolivro.validation

import com.mercadolivro.service.CustomerService
import org.hibernate.validator.internal.engine.validationcontext.ValidationContext
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import kotlin.reflect.KClass

class EmailAvailableValidator( var customerService: CustomerService): ConstraintValidator<EmailAvailable, String> {
    override fun isValid(
        value: String,
        context: ConstraintValidatorContext?): Boolean {
        if(value.isNullOrEmpty()) return  false
        return  customerService.emailAvailable(value)
    }
}
