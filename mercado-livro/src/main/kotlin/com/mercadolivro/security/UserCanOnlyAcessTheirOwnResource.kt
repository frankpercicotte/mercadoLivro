package com.mercadolivro.security

import org.springframework.security.access.prepost.PreAuthorize

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@PreAuthorize("hasRole('ADMIN') || #id == authentication.principal.id")
annotation class UserCanOnlyAccessTheirOwnResource
