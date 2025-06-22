package com.mercadolivro.service

import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.exceptions.NotFoundException
import com.mercadolivro.exceptions.NotPutException
import com.mercadolivro.repository.CustomerRepository
import com.mercadolivro.utils.buildCustomer
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*

@ExtendWith(MockKExtension::class)
class CustomecrServiceTest {
    @MockK
    private lateinit var customerRepository: CustomerRepository
    @MockK
    private lateinit var bookService: BookService
    @MockK
    private lateinit var bCrypt: BCryptPasswordEncoder
    @InjectMockKs
    @SpyK
    private lateinit var customerService: CustomerService

    @Test
    fun `should return all customers success when name is not informed`() {
        val pageable = PageRequest.of(0, 10)
        val fakeCustomers = listOf(buildCustomer(), buildCustomer())
        val totalRegistry : Long = 2L
        val page = PageImpl(fakeCustomers, pageable, totalRegistry)

        every { customerRepository.findAll(pageable) } returns page

        val customers = customerService.getAll(null, pageable)

        assertEquals(page, customers)
        verify(exactly = 1) { customerRepository.findAll(pageable) }
        verify(exactly = 0) { customerRepository.findByNameContaining(any(), any()) }
    }

    @Test
    fun `should return customer success when name is informed`() {
        val pageable = PageRequest.of(0, 10)
        val name = UUID.randomUUID().toString()
        val fakeCustomer = listOf(buildCustomer(name = name))
        val totalRegistry : Long = 2L
        val page = PageImpl(fakeCustomer, pageable, totalRegistry)

        every { customerRepository.findByNameContaining( name,pageable) } returns page

        val customer = customerService.getAll(name, pageable)

        assertEquals(page, customer)
        verify(exactly = 0) { customerRepository.findAll(pageable) }
        verify(exactly = 1) { customerRepository.findByNameContaining(any(), any()) }
    }

    @Test
    fun `should create customer and encrypt password`() {
        val initialPassword = Random().nextInt().toString()
        val fakeCustomer = buildCustomer(password = initialPassword)
        val fakePassword = UUID.randomUUID().toString()
        val fakeCustomerEncrypted = fakeCustomer.copy(password = fakePassword)

        every { customerRepository.save(fakeCustomerEncrypted) } returns fakeCustomer
        every { bCrypt.encode(initialPassword) } returns fakePassword

        customerService.create(fakeCustomer)

        verify(exactly = 1) { customerRepository.save(fakeCustomerEncrypted) }
        verify(exactly = 1) { bCrypt.encode(initialPassword) }
    }

    @Test
    fun `should return customer success when findById`() {
        val id = Random().nextInt()
        val fakeCustomer = buildCustomer(id = id)

        every { customerRepository.findById(id) } returns Optional.of(fakeCustomer)

        val customer = customerService.findById(id)

        assertEquals(fakeCustomer, customer)
        verify(exactly = 1) { customerRepository.findById(id) }
    }

    @Test
    fun `should throw error when findById and customer not found`() {
        val id = Random().nextInt()

        every { customerRepository.findById(id) } returns Optional.empty()

        val error = assertThrows<NotFoundException>{
            customerService.findById(id)
        }

        assertEquals("Customer not found, ID: ${id}.", error.message)
        verify(exactly = 1) { customerRepository.findById(id) }
    }

    @Test
    fun `should update customer success when id exist`() {
        val id = Random().nextInt()
        val fakeCustomer = buildCustomer(id = id)

        every { customerRepository.existsById(id) } returns true
        every { customerRepository.findById(id) } returns Optional.of(fakeCustomer)
        every { customerRepository.save(fakeCustomer) } returns fakeCustomer
        every { customerRepository.existsByName(fakeCustomer.name) } returns false
        every { customerRepository.existsByEmail(fakeCustomer.email) } returns false

        customerService.update(fakeCustomer)

        verify(exactly = 1) { customerRepository.existsById(id) }
        verify(exactly = 1) { customerRepository.save(fakeCustomer) }
    }

    @Test
    fun `should update customer when name and email change and are free`() {
        val id = Random().nextInt()
        val customerOld = buildCustomer(
            id = id,
            name = "Alice",
            email = "alice@email.com"
        )
        val customerUpdated = customerOld.copy(
            name = "Alice Wonderland",
            email = "alice.wonderland@email.com"
        )

        every { customerRepository.existsById(id) } returns true
        every { customerRepository.findById(id) } returns Optional.of(customerOld)
        every { customerRepository.existsByName(customerUpdated.name) } returns false
        every { customerRepository.existsByEmail(customerUpdated.email) } returns false
        every { customerRepository.save(customerUpdated) } returns customerUpdated

        assertDoesNotThrow {
            customerService.update(customerUpdated)
        }

        verifySequence {
            customerRepository.existsById(id)
            customerRepository.findById(id)
            customerRepository.existsByName(customerUpdated.name)
            customerRepository.existsByEmail(customerUpdated.email)
            customerRepository.save(customerUpdated)
        }
    }

    @Test
    fun `should update customer exception when name and email change and are not free`() {
        val id = Random().nextInt()
        val customerOld = buildCustomer(
            id = id,
            name = "Alice",
            email = "alice@email.com"
        )
        val customerUpdated = customerOld.copy(
            name = "Alice Wonderland",
            email = "alice.wonderland@email.com"
        )

        every { customerRepository.existsById(id) } returns true
        every { customerRepository.findById(id) } returns Optional.of(customerOld)
        every { customerRepository.existsByName(customerUpdated.name) } returns false
        every { customerRepository.existsByEmail(customerUpdated.email) } returns true

        val error = assertThrows<NotPutException> {
            customerService.update(customerUpdated)
        }

        assertEquals("The name or email are in use.", error.message)

        verifySequence {
            customerRepository.existsById(id)
            customerRepository.findById(id)
            customerRepository.existsByName(customerUpdated.name)
            customerRepository.existsByEmail(customerUpdated.email)
        }
    }

    @Test
    fun `should update customer exception when name and name change and are not free`() {
        val id = Random().nextInt()
        val customerOld = buildCustomer(
            id = id,
            name = "Alice",
            email = "alice@email.com"
        )
        val customerUpdated = customerOld.copy(
            name = "Alice Wonderland",
            email = "alice.wonderland@email.com"
        )

        every { customerRepository.existsById(id) } returns true
        every { customerRepository.findById(id) } returns Optional.of(customerOld)
        every { customerRepository.existsByName(customerUpdated.name) } returns true
        every { customerRepository.existsByEmail(customerUpdated.email) } returns false

        val error = assertThrows<NotPutException> {
            customerService.update(customerUpdated)
        }

        assertEquals("The name or email are in use.", error.message)

        verifySequence {
            customerRepository.existsById(id)
            customerRepository.findById(id)
            customerRepository.existsByName(customerUpdated.name)
        }
    }

    @Test
    fun `should throw not found exception when update customer`() {
        val id = Random().nextInt()
        val fakeCustomer = buildCustomer(id = id)

        every { customerRepository.existsById(id) } returns false
        every { customerRepository.save(fakeCustomer) } returns fakeCustomer

        val error = assertThrows<NotFoundException>{
            customerService.update(fakeCustomer)
        }

        assertEquals("Customer not found, ID: ${id}.", error.message)

        verify(exactly = 1) { customerRepository.existsById(id) }
        verify(exactly = 0) { customerRepository.save(any()) }
    }



    @Test
    fun `should delete customer`() {
        val id = Random().nextInt()
        val fakeCustomer = buildCustomer(id = id)
        val expectedCustomer = fakeCustomer.copy(status = CustomerStatus.INATIVO)

        every { customerRepository.existsById(id) } returns true
        every { customerRepository.findById(id) } returns Optional.of(fakeCustomer)
        every { customerRepository.save(expectedCustomer) } returns expectedCustomer
        every { bookService.deleteByCustomer(fakeCustomer) } just runs

        customerService.delete(fakeCustomer.id!!)

        verify(exactly = 1) { customerService.findById(id) }
        verify(exactly = 1) { bookService.deleteByCustomer(fakeCustomer) }
        verify(exactly = 1) { customerRepository.save(expectedCustomer) }
    }


    @Test
    fun `should throw not found exception when delete customer`() {
        val id = Random().nextInt()

        every { customerRepository.existsById(id)} returns false

        val error = assertThrows<NotFoundException>{
            customerService.delete(id)
        }

        assertEquals("Customer not found, ID: ${id}.", error.message)

        verify(exactly = 1) { customerRepository.existsById(id) }
        verify(exactly = 0) { bookService.deleteByCustomer(any()) }
        verify(exactly = 0) { customerRepository.save(any()) }
    }

    @Test
    fun `should return true when email available`() {
        val email = "${Random().nextInt().toString()}@email.com"

        every { customerRepository.existsByEmail(email) } returns false

        val emailAvailable = customerService.emailAvailable(email)

        assertTrue(emailAvailable)
        verify(exactly = 1) { customerRepository.existsByEmail(email) }

    }

    @Test
    fun `should return false when email unavailable`() {
        val email = "${Random().nextInt()}@email.com"

        every { customerRepository.existsByEmail(email) } returns true

        val emailAvailable = customerService.emailAvailable(email)

        assertFalse(emailAvailable)
        verify(exactly = 1) { customerRepository.existsByEmail(email) }

    }

}