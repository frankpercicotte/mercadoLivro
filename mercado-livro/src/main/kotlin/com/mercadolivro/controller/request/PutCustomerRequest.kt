package com.mercadolivro.controller.request

import com.mercadolivro.exceptions.ErrorMessageConstants
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

data class PutCustomerRequest (
    @field:NotEmpty(message = ErrorMessageConstants.NAME_REQUIRED)
    var name: String,
    @field:Email(message = ErrorMessageConstants.EMAIL_INVALID)
    var email: String
)