package com.mercadolivro.exceptions

object ErrorMessageConstants {
    fun invalidRequestMessage(objectName: String, errorCount: Int): String {
        return "Invalid request in object '$objectName': $errorCount field(s) with error(s)."
    }

    const val NAME_REQUIRED = "The name field is required."
    const val EMAIL_INVALID = "The email provided is invalid."
    const val PRICE_NOT_NULL = "The price cannot be null."
}