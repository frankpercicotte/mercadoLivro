package com.mercadolivro.repository

import com.mercadolivro.enums.BookStatus
import com.mercadolivro.model.BookModel
import com.mercadolivro.model.CustomerModel
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository

//interface BookRepository : CrudRepository<BookModel, Int> { --> changed to JpaRepository that we don´t need to modify findAll()
interface BookRepository : JpaRepository<BookModel, Int> {

    fun findByNameContaining(name: String): List<BookModel>
    fun findByStatus(status: BookStatus, pageable: Pageable): Page<BookModel>
    fun findByCustomer(customer: CustomerModel): List<BookModel>
    //fun findAll(pageable: Pageable): Page<BookModel> --> It is used when CrudRepository
}