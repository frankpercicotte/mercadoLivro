package com.mercadolivro.exceptions

class NotFoundException(override val message: String) : RuntimeException(message)