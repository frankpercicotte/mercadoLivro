package com.mercadolivro.repository

import com.mercadolivro.model.CustomerModel
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository

//interface CustomerRepository : CrudRepository<CustomerModel, Int> {
interface CustomerRepository : JpaRepository<CustomerModel, Int> {

    fun findByNameContaining(name: String, pageable: Pageable): Page<CustomerModel>

}