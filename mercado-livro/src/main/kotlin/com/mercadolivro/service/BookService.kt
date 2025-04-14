package com.mercadolivro.service

import com.mercadolivro.controller.request.PutBookRequest
import com.mercadolivro.enums.BookStatus
import com.mercadolivro.extension.toBookModel
import com.mercadolivro.model.BookModel
import com.mercadolivro.repository.BookRepository
import com.mercadolivro.exceptions.NotFoundException
import org.springframework.stereotype.Service


@Service
class BookService(
    val bookRepository: BookRepository
) {

    fun create(book: BookModel) {
        bookRepository.save(book)
    }

    fun findAll(): List<BookModel> = bookRepository.findAll().toList()

    fun findActives(): List<BookModel> = bookRepository.findByStatus(BookStatus.ATIVO).toList()

    fun findById(id: Int): BookModel{
        return bookRepository.findById(id).orElseThrow(){ NotFoundException("Book with id $id not found") }
    }

    fun update(id: Int, book: PutBookRequest) {
        val bookSaved = findById(id)
        bookRepository.save(book.toBookModel(bookSaved))
    }

    fun delete(id: Int) {
        //Never delete book, only change status to CANCELADO
        val book : BookModel = findById(id)
        bookRepository.save(book)
    }

}
