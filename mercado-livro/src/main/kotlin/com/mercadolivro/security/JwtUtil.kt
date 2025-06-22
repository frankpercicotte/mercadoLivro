package com.mercadolivro.security

import com.mercadolivro.enums.Errors
import com.mercadolivro.exceptions.CustomAuthenticationException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.MacAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
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

    fun isValidToken(token: String): Boolean{
        return try {
            val claims = getClaims(token)
            claims.subject != null &&
                    claims.expiration != null &&
                    Date().before(claims.expiration)
        } catch (ex: Exception) {
            throw CustomAuthenticationException(ex.toString())
        }
    }

    private fun getClaims(token: String): Claims {
        try {
            val key = Keys.hmacShaKeyFor(secret!!.toByteArray(StandardCharsets.UTF_8))
            return Jwts
                .parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .payload
        } catch (ex: Exception) {
            throw CustomAuthenticationException(Errors.AuthenticationTokenException.toString())
        }
    }

    fun getSubject(token: String): String {
        return getClaims(token).subject
    }
}
