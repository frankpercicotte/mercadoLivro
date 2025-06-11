package com.mercadolivro.controller.response


import io.swagger.v3.oas.models.Paths
import java.time.LocalDateTime

data class ErrorResponse(
    val message: String,
    val status: Int,
    val timestamp: LocalDateTime,
    val errors: List<FieldErrorResponse>?,
    val path: String? = null
)
