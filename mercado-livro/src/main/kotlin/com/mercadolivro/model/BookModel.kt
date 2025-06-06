package com.mercadolivro.model

import com.mercadolivro.enums.BookStatus
import java.math.BigDecimal
import javax.persistence.*

@Entity(name = "book")
data class BookModel(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @Column
    var name: String,

    @Column
    var price: BigDecimal,

    @ManyToOne
    @JoinColumn(name = "customer_id")
    var customer: CustomerModel?
){
    @Column
    @Enumerated(EnumType.STRING)
    var status: BookStatus? = null
        set(value) {
            if(field == BookStatus.CANCELADO || field == BookStatus.DELETADO){
                throw Exception("Can't change status of the book when status ${field}!")
            }
            field = value
        }

    constructor(
            id: Int? = null,
            name: String,
            price: BigDecimal,
            customer: CustomerModel? = null,
            status: BookStatus?):this(id, name, price, customer) {
        this.status = status
    }
}