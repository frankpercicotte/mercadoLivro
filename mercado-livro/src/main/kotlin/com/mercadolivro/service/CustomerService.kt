package com.mercadolivro.service

import com.mercadolivro.exceptions.NotFoundException
import com.mercadolivro.model.CustomerModel
import com.mercadolivro.repository.CustomerRepository
import org.springframework.stereotype.Service

@Service
class CustomerService(
    val customerRepository: CustomerRepository
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

    fun getById(id: Int): CustomerModel {
        return customerRepository.findById(id).orElseThrow()
    }

    fun update(customer: CustomerModel) {
        ensureCustomerExists(customer.id)
        customerRepository.save(customer)
    }

    fun delete(id: Int) {
        ensureCustomerExists(id)
        customerRepository.deleteById(id)
    }

    private fun ensureCustomerExists(id: Int?) {
        if (id == null || !customerRepository.existsById(id)) {
            throw NotFoundException("Customer not found, ID $id")
        }
    }
}