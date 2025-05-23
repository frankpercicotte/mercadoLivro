package com.mercadolivro.controller

import com.mercadolivro.controller.request.PostBookRequest
import com.mercadolivro.controller.request.PutBookRequest
import com.mercadolivro.controller.response.BookResponse
import com.mercadolivro.extension.toBookModel
import com.mercadolivro.extension.toResponse
import com.mercadolivro.service.BookService
import com.mercadolivro.service.CustomerService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("books")
class BookController(
    val customerService: CustomerService,
    val bookService: BookService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody @Valid request: PostBookRequest) {
        val customer = customerService.findById(request.customerId)
        bookService.create(request.toBookModel(customer))
    }

    @GetMapping
    fun findAll(@PageableDefault(page = 0, size = 10) pageable: Pageable): Page<BookResponse> {
        return bookService.findAll(pageable).map{ it.toResponse()}
    }

    @GetMapping("/actives")
    fun findActives(@PageableDefault(page = 0, size = 10) pageable: Pageable): Page<BookResponse> {
        return  bookService.findActives(pageable).map{it.toResponse()}
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Int): BookResponse{
        return bookService.findById(id).toResponse()
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun putActives(@PathVariable id: Int, @RequestBody book : PutBookRequest){
        bookService.update(id, book)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteBook(@PathVariable id: Int){
        return bookService.delete(id)
    }
}