package com.mercadolivro.validation

import com.mercadolivro.service.CustomerService
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class NameAvailableValidator(var customerService: CustomerService): ConstraintValidator<NameAvailable, String> {
    override fun isValid(
        value: String,
        context: ConstraintValidatorContext?): Boolean {
        if(value.isNullOrEmpty()) return  false
        return  customerService.nameAvailable(value)
    }
}
