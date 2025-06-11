package com.mercadolivro.config

import com.mercadolivro.enums.Role
import com.mercadolivro.repository.CustomerRepository
import com.mercadolivro.security.AuthenticationFilter
import com.mercadolivro.security.AuthorizationFilter
import com.mercadolivro.security.CustomAuthenticationEntryPoint
import com.mercadolivro.security.JwtUtil
import com.mercadolivro.service.UserDetailsCustomService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration


@Configuration
class SecurityConfig(
    private val customerRepository: CustomerRepository,
    private val userDetails: UserDetailsCustomService,
    private val jwtUtil: JwtUtil,
    private val customAuthenticationEntryPoint: CustomAuthenticationEntryPoint
    ) {
    //PUBLIC_MATCHERS used to open url with all type: POST,GET,PUT...
    private val PUBLIC_MATCHERS = arrayOf<String>()
    private val PUBLIC_POST_MATCHERS = arrayOf("/customer")
    private val ADMIN_MATCHERS = arrayOf("/admin/**")
    private val PUBLIC_GET_MATCHERS = arrayOf("/books","/books/actives")

    fun configure(auth: AuthenticationManagerBuilder){
        auth.userDetailsService(userDetails).passwordEncoder(bCryptPasswordEncoder())
    }
    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder{
        return BCryptPasswordEncoder()
    }

    @Bean
    fun filterChain(http: HttpSecurity, authenticationManager: AuthenticationManager): SecurityFilterChain {
        http.cors().and().csrf().disable()
        http.authorizeRequests()
            .antMatchers(*PUBLIC_MATCHERS).permitAll()
            .antMatchers(HttpMethod.POST,*PUBLIC_POST_MATCHERS).permitAll()
            .antMatchers(*ADMIN_MATCHERS).hasAuthority(Role.ADMIN.description)
            //.antMatchers(HttpMethod.GET,*PUBLIC_GET_MATCHERS).permitAll()
            .anyRequest().authenticated()
        http.addFilter(AuthenticationFilter(authenticationManager, customerRepository, jwtUtil))
        http.addFilter(AuthorizationFilter(authenticationManager,userDetails, jwtUtil, customAuthenticationEntryPoint))
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        http.exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint)
        return http.build()
    }

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

}
