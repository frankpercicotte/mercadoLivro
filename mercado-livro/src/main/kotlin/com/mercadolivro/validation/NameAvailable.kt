package com.mercadolivro.validation

import com.mercadolivro.exceptions.ErrorMessageConstants
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Constraint(validatedBy = [NameAvailableValidator::class])
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class NameAvailable(
    val message: String = ErrorMessageConstants.NAME_USED,
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
