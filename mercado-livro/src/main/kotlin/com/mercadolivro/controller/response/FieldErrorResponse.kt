package com.mercadolivro.controller.response

data class FieldErrorResponse(
    val message: String,
    var field: String
)