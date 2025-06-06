package com.mercadolivro.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.MacAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtUtil {

    @Value("\${jwt.expiration}")
    private  val expiration: Long? = null
    @Value("\${jwt.secret}")
    private val secret: String? = null

    fun generateToken(id: Int?): String {
        val now = Date()
        val expiryDate = Date(now.time + (expiration ?: 99999L))

        val key: SecretKey = Keys.hmacShaKeyFor(secret?.toByteArray() ?:"default-secret".toByteArray())
        val alg: MacAlgorithm = Jwts.SIG.HS256

        return Jwts.builder()
            .subject(id.toString())
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(key, alg)
            .compact()
    }

}