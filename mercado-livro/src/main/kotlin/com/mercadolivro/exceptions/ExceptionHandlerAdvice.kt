package com.mercadolivro.exceptions
import com.mercadolivro.controller.response.ErrorResponse
import com.mercadolivro.controller.response.FieldErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
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
            timestamp = LocalDateTime.now(),
            null
        )
        return ResponseEntity(error, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleNew(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val objectName = ex.bindingResult.objectName
        val fieldErrorCount = ex.bindingResult.fieldErrors.size
        val error = ErrorResponse(
            message = ErrorMessageConstants.invalidRequestMessage(objectName, fieldErrorCount),
            status = HttpStatus.BAD_REQUEST.value(),
            timestamp = LocalDateTime.now(),
            ex.bindingResult.fieldErrors.map{ FieldErrorResponse(it.defaultMessage ?: "invalid", it.field )}

        )
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(NotDeleteException::class)
    fun handleNotDelete(ex: NotDeleteException): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            message = ex.message ?: "Resource cannot delete.",
            status = HttpStatus.CONFLICT.value(),
            timestamp = LocalDateTime.now(),
            null

        )
        return ResponseEntity(error, HttpStatus.CONFLICT)
    }

    @ExceptionHandler(NotPutException::class)
    fun handleNotPut(ex: NotPutException): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            message = ex.message ?: "Resource cannot update.",
            status = HttpStatus.CONFLICT.value(),
            timestamp = LocalDateTime.now(),
            null

        )
        return ResponseEntity(error, HttpStatus.CONFLICT)
    }
}