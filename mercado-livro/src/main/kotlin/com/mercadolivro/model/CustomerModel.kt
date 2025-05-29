package com.mercadolivro.model

import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.enums.Profile
import javax.persistence.*

@Entity(name = "customer")
data class CustomerModel(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @Column
    var name: String,

    @Column
    var email: String,

    @Column
    @Enumerated(EnumType.STRING)
    var status: CustomerStatus,

    @Column
    val password: String,

    @Column
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "customer_role", joinColumns = [JoinColumn(name = "customer_id")])
    @ElementCollection(targetClass = Profile::class, fetch = FetchType.EAGER)
    val role: Set<Profile> = setOf()
)