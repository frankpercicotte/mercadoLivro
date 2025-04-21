package com.mercadolivro.service

import com.mercadolivro.controller.request.PutBookRequest
import com.mercadolivro.enums.BookStatus
import com.mercadolivro.enums.Erros
import com.mercadolivro.extension.toBookModel
import com.mercadolivro.model.BookModel
import com.mercadolivro.repository.BookRepository
import com.mercadolivro.exceptions.NotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service


@Service
class BookService(
    val bookRepository: BookRepository
) {

    fun create(book: BookModel) {
        bookRepository.save(book)
    }

    fun findAll(pageable: Pageable): Page<BookModel> = bookRepository.findAll(pageable)

    fun findActives(pageable: Pageable): Page<BookModel> = bookRepository.findByStatus(BookStatus.ATIVO, pageable)

    fun findById(id: Int): BookModel{
        return bookRepository.findById(id).orElseThrow(){ NotFoundException(Erros.BookNotFound.message.format(id)) }
    }

    fun update(id: Int, book: PutBookRequest) {
        val bookSaved = findById(id)
        bookRepository.save(book.toBookModel(bookSaved))
    }

    fun delete(id: Int) {
        //Never delete book, only change status to DELETADO
        val book : BookModel = findById(id)
        book.status = BookStatus.DELETADO
        bookRepository.save(book)
    }

    //Books with 'DELETADO, CANCELADO, VENDIDO' status can´t change status.
    fun deleteByCustomer(customer: com.mercadolivro.model.CustomerModel) {
        val books = bookRepository.findByCustomer(customer)
        val booksSet = setOf<BookStatus>(BookStatus.DELETADO, BookStatus.CANCELADO, BookStatus.VENDIDO)
        for(book in books){
            if(!booksSet.contains(book.status)){
                book.status = BookStatus.DELETADO
            }
        }
        bookRepository.saveAll(books)
    }

}
