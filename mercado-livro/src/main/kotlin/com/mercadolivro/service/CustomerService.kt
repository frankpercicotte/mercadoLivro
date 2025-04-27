package com.mercadolivro.service

import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.enums.Erros
import com.mercadolivro.exceptions.ErrorMessageConstants
import com.mercadolivro.exceptions.NotFoundException
import com.mercadolivro.exceptions.NotPutException
import com.mercadolivro.model.CustomerModel
import com.mercadolivro.repository.CustomerRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class CustomerService(
    val customerRepository: CustomerRepository,
    val bookService: BookService
) {

    fun getAll(name: String?, pageable: Pageable): Page<CustomerModel> {
        name?.let {
            return customerRepository.findByNameContaining(it, pageable)
        }
        return customerRepository.findAll(pageable)
    }

    fun create(customer: CustomerModel) {
        customerRepository.save(customer)
    }

    fun findById(id: Int): CustomerModel {
        return customerRepository.findById(id).orElseThrow{NotFoundException(Erros.CustomerNotFound.message.format(id))}
    }

    fun update(customer: CustomerModel) {
        ensureCustomerExists(customer.id)
        ensureCustomerPutEmailOrNameExists(customer)

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
            throw NotFoundException(Erros.CustomerNotFound.message.format(id))
        }
    }

    private fun ensureCustomerPutEmailOrNameExists(customer: CustomerModel){
        val customerOld = findById(customer.id!!)
        if(customerOld.name != customer.name){
            if(!nameAvailable(customer.name)) throw NotPutException(ErrorMessageConstants.NAME_OR_EMAIL_USED)
        }
        if(customerOld.email != customer.email){
            if(!emailAvailable(customer.email)) throw NotPutException(ErrorMessageConstants.NAME_OR_EMAIL_USED)
        }
    }

    fun emailAvailable(email: String): Boolean = !customerRepository.existsByEmail(email)
    fun nameAvailable(name: String): Boolean = !customerRepository.existsByName(name)
}

