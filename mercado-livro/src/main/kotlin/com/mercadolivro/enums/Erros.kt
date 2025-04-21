package com.mercadolivro.enums

enum class Erros(val message: String) {
    CustomerNotFound("Customer not found, ID: %s"),
    BookNotFound("Book not found, ID: %s"),
}