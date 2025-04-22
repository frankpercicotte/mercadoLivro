package com.mercadolivro.controller.response


import java.time.LocalDateTime

data class ErrorResponse(
    val message: String,
    val status: Int,
    val timestamp: LocalDateTime,
    val errors: List<FieldErrorResponse>?
)
