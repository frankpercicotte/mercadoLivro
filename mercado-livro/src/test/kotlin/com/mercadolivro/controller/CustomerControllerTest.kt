package com.mercadolivro.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.mercadolivro.controller.request.PostCustomerRequest
import com.mercadolivro.controller.request.PutCustomerRequest
import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.enums.Role
import com.mercadolivro.repository.CustomerRepository
import com.mercadolivro.security.JwtUtil
import com.mercadolivro.security.UserCustomDetails
import com.mercadolivro.utils.buildCustomer
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import kotlin.random.Random

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@ActiveProfiles("test")
@WithMockUser
class CustomerControllerTest {

    companion object {
        const val BASE_URL = "/customer"
    }

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var jwtUtil: JwtUtil

    @BeforeEach
    fun cleanup() {
        customerRepository.deleteAll()
    }

    @Test
    fun `should return all customers when get all`() {
        val customer1 = customerRepository.save(buildCustomer())
        val customer2 = customerRepository.save(buildCustomer())

        mockMvc.perform(get(BASE_URL))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content.length()").value(2))
            .andExpect(jsonPath("$.content[0].id").value(customer1.id))
            .andExpect(jsonPath("$.content[1].id").value(customer2.id))
    }

    @Test
    fun `should filter all customers by name when get all`() {
        val frank = customerRepository.save(buildCustomer(name = "Franklin"))
        customerRepository.save(buildCustomer(name = "Luiza"))

        mockMvc.perform(get("$BASE_URL?name=Frank"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content.length()").value(1))
            .andExpect(jsonPath("$.content[0].id").value(frank.id))
    }

    @Test
    fun `should create customer`() {
        val request = PostCustomerRequest("fake name", "${Random.nextInt()}@fakeemail.com", "123456")
        mockMvc.perform(post(BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated)

        val customers = customerRepository.findAll()
        Assertions.assertEquals(1, customers.size)
        Assertions.assertEquals(request.name, customers[0].name)
    }

    @Test
    fun `should throw error when create customer has invalid information`() {
        val request = PostCustomerRequest("", "${Random.nextInt()}@fakeemail.com", "123456")
        mockMvc.perform(post(BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.errors[0].field").value("name"))
    }

    @Test
    fun `should get user by id when user has the same id`() {
        val customer = customerRepository.save(buildCustomer())

        mockMvc.perform(get("$BASE_URL/${customer.id}")
            .with(user(UserCustomDetails(customer))))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(customer.id))
    }

    @Test
    fun `should return forbidden when authenticated user tries to access another customer`() {
        val user1 = customerRepository.save(buildCustomer(id = 25))
        val user2 = customerRepository.save(buildCustomer(id = 26))
        val token = jwtUtil.generateToken(user1.id)

        mockMvc.perform(get("$BASE_URL/${user2.id}")
            .header("Authorization", "Bearer $token"))
            .andExpect(status().isForbidden)
    }

    @Test
    fun `should get user by id when user is admin`() {
        val customer = customerRepository.save(buildCustomer())

        mockMvc.perform(get("$BASE_URL/${customer.id}")
            .with(user("admin").roles("ADMIN")))
            .andExpect(status().isOk)
    }

    @Test
    fun `should update customer`() {
        val customer = customerRepository.save(buildCustomer())
        val request = PutCustomerRequest("Franklin", "emailupdate@email.com")

        mockMvc.perform(put("$BASE_URL/${customer.id}")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNoContent)

        val updated = customerRepository.findById(customer.id!!).get()
        Assertions.assertEquals(request.name, updated.name)
        Assertions.assertEquals(request.email, updated.email)
    }

    @Test
    fun `should return not found when update customer not existing`() {
        val request = PutCustomerRequest("Franklin", "email@email.com")

        mockMvc.perform(put("$BASE_URL/9999")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `should throw error when update customer has invalid information`() {
        val customer = customerRepository.save(buildCustomer())
        val request = PutCustomerRequest("", "email@email.com")

        mockMvc.perform(put("$BASE_URL/${customer.id}")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should delete customer`() {
        val customer = customerRepository.save(buildCustomer())

        mockMvc.perform(delete("$BASE_URL/${customer.id}"))
            .andExpect(status().isNoContent)

        val deleted = customerRepository.findById(customer.id!!).get()
        Assertions.assertEquals(CustomerStatus.INATIVO, deleted.status)
    }

    @Test
    fun `should return not found when delete customer not exists`() {
        mockMvc.perform(delete("$BASE_URL/9999"))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.status").value(404))
    }
}
