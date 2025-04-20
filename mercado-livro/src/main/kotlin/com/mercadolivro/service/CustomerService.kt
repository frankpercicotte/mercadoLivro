package com.mercadolivro.service

import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.exceptions.NotFoundException
import com.mercadolivro.model.CustomerModel
import com.mercadolivro.repository.CustomerRepository
import org.springframework.stereotype.Service

@Service
class CustomerService(
    val customerRepository: CustomerRepository,
    val bookService: BookService
) {

    fun getAll(name: String?): List<CustomerModel> {
        name?.let {
            return customerRepository.findByNameContaining(it)
        }
        return customerRepository.findAll().toList()
    }

    fun create(customer: CustomerModel) {
        customerRepository.save(customer)
    }

    fun findById(id: Int): CustomerModel {
        return customerRepository.findById(id).orElseThrow()
    }

    fun update(customer: CustomerModel) {
        ensureCustomerExists(customer.id)
        customerRepository.save(customer)
    }

    fun delete(id: Int) {
        ensureCustomerExists(id)
        val customer = findById(id)
        bookService.deleteByCustomer(customer)
        customer.status = CustomerStatus.INATIVO
        customerRepository.save(customer)
    }

    private fun ensureCustomerExists(id: Int?) {
        if (id == null || !customerRepository.existsById(id)) {
            throw NotFoundException("Customer not found, ID $id")
        }
    }
}