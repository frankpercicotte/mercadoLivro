package com.mercadolivro.security
import com.mercadolivro.enums.Erros
import com.mercadolivro.exceptions.CustomAuthenticationException
import com.mercadolivro.service.UserDetailsCustomService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthorizationFilter(
    authenticationManager: AuthenticationManager,
    private val userDetails: UserDetailsCustomService,
    private val jwtUtil: JwtUtil,
    private val authenticationEntryPoint: AuthenticationEntryPoint
) : BasicAuthenticationFilter(authenticationManager) {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        try {
            val authorization = request.getHeader("Authorization")
            if (authorization != null && authorization.startsWith("Bearer ")) {
                val token = authorization.substringAfter("Bearer ")
                val auth = getAuthentication(token)
                SecurityContextHolder.getContext().authentication = auth
            }
            chain.doFilter(request, response)
        } catch (ex: CustomAuthenticationException) {
            authenticationEntryPoint.commence(request, response, ex)
        }
    }

    private fun getAuthentication(token: String): UsernamePasswordAuthenticationToken {
        if(!jwtUtil.isValidToken(token)) {
            throw CustomAuthenticationException(Erros.AuthenticationTokenException.toString())
        }
        val subject = jwtUtil.getSubject(token)
        val customer = userDetails.loadUserByUsername(subject)
        return UsernamePasswordAuthenticationToken(subject, null, customer.authorities)
    }

}