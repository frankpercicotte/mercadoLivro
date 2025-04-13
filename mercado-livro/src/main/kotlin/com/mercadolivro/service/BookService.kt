package com.mercadolivro.service

import com.mercadolivro.enums.BookStatus
import com.mercadolivro.model.BookModel
import com.mercadolivro.repository.BookRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class BookService(
    val bookRepository: BookRepository
) {

    fun create(book: BookModel) {
        bookRepository.save(book)
    }

    fun findAll(): List<BookModel> = bookRepository.findAll().toList()

    fun findActives(): List<BookModel> = bookRepository.findByStatus(BookStatus.ATIVO).toList()

    fun findById(id: Int): BookModel = bookRepository.findById(id).orElseThrow()

    fun putBook(id : Int, book: BookModel): BookModel {
        var findBook : BookModel = bookRepository.findById(id).orElseThrow()
        findBook = book
        bookRepository.save(findBook)
        return book
    }

    fun deleteBook() {
        return
    }



}
