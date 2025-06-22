package com.mercadolivro.enums

enum class Errors(val message: String) {
    CustomerNotFound("Customer not found, ID: %s."),
    BookNotFound("Book not found, ID: %s."),
    BookCantBeDelete("This book [ %s ] can't be delete, because status is %s."),
    AuthenticationException("Authentication Fail."),
    UserNotFound("User not Found."),
    AuthenticationTokenException("Invalid authentication token.")
}