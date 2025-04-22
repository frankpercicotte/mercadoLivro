package com.mercadolivro.controller.request

import com.fasterxml.jackson.annotation.JsonAlias
import com.mercadolivro.exceptions.ErrorMessageConstants
import java.math.BigDecimal
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class PostBookRequest (
    @field:NotEmpty(message = ErrorMessageConstants.NAME_REQUIRED)
    var name: String,
    @field:NotNull(message = ErrorMessageConstants.PRICE_NOT_NULL)
    var price: BigDecimal,
    @JsonAlias("customer_id")
    @field:NotNull(message = "test mandatory customerId")
    var customerId: Int
)
