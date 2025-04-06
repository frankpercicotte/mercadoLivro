package com.mercadolivro.exceptions
import com.mercadolivro.exceptions.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

@RestControllerAdvice
class ExceptionHandlerAdvice {
    @ExceptionHandler(NotFoundException::class)
    fun handleNotFound(ex: NotFoundException): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            message = ex.message ?: "Resource not found.",
            status = HttpStatus.NOT_FOUND.value(),
            timestamp = LocalDateTime.now()
        )
        return ResponseEntity(error, HttpStatus.NOT_FOUND)
    }
}

data class ErrorResponse(
        val message: String,
        val status: Int,
        val timestamp: LocalDateTime
)
