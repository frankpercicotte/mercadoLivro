package com.mercadolivro.controller.request

import com.mercadolivro.exceptions.ErrorMessageConstants
import com.mercadolivro.model.CustomerModel
import com.mercadolivro.validation.EmailAvailable
import com.mercadolivro.validation.NameAvailable
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

data class PostCustomerRequest (
    @field:NotEmpty(message = ErrorMessageConstants.NAME_REQUIRED)
    @NameAvailable()
    var name: String,
    @field:Email(message = ErrorMessageConstants.EMAIL_INVALID)
    @EmailAvailable()
    var email: String
)