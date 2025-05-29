package com.mercadolivro.exceptions

object ErrorMessageConstants {
    fun invalidRequestMessage(objectName: String, errorCount: Int): String {
        return "Invalid request in object '$objectName': $errorCount field(s) with error(s)."
    }

    const val NAME_OR_EMAIL_USED = "The name or email are in use."
    const val NAME_REQUIRED = "The name field is required."
    const val NAME_USED = "This name is already in use."
    const val EMAIL_INVALID = "The email provided is invalid."
    const val EMAIL_USED = "Email is already in use."
    const val PRICE_NOT_NULL = "The price cannot be null."
    const val PASSWORD_REQUIRED = "The password field is reequired"
}