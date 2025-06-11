package com.mercadolivro.exceptions

import org.springframework.security.core.AuthenticationException


class CustomAuthenticationException(message: String): AuthenticationException(message){
}