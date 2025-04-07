package com.mercadolivro.repository

import com.mercadolivro.model.CustomerModel
import org.springframework.data.repository.CrudRepository

interface BookRepository : CrudRepository<CustomerModel, Int> {

    fun findByNameContaining(name: String): List<CustomerModel>

}