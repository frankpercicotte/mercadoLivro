package com.mercadolivro.validation

import com.mercadolivro.exceptions.ErrorMessageConstants
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Constraint(validatedBy = [EmailAvailableValidator::class])
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class EmailAvailable(
    val message: String = ErrorMessageConstants.EMAIL_USED,
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
