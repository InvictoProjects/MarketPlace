package com.invictoprojects.marketplace.controller

import com.invictoprojects.marketplace.exception.ErrorMessage
import com.invictoprojects.marketplace.exception.InvalidCredentialsException
import com.invictoprojects.marketplace.exception.NotEnoughPermissionException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.Instant
import javax.persistence.EntityNotFoundException
import javax.servlet.http.HttpServletRequest

@ControllerAdvice
class ExceptionHandler {
    @ExceptionHandler(
        IllegalArgumentException::class,
        EntityNotFoundException::class,
        InvalidCredentialsException::class,
        UsernameNotFoundException::class
    )
    fun handleException(ex: Exception, request: HttpServletRequest): ResponseEntity<ErrorMessage> {
        val status = when (ex) {
            is IllegalArgumentException -> HttpStatus.BAD_REQUEST
            is EntityNotFoundException -> HttpStatus.NOT_FOUND
            is InvalidCredentialsException -> HttpStatus.UNAUTHORIZED
            is UsernameNotFoundException -> HttpStatus.NOT_FOUND
            is NotEnoughPermissionException -> HttpStatus.FORBIDDEN
            else -> HttpStatus.INTERNAL_SERVER_ERROR
        }
        return buildResponseEntity(status, listOf(ex.message), request)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(
        ex: MethodArgumentNotValidException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorMessage> {
        val messages = ex.bindingResult.allErrors.map { it.defaultMessage }
        return buildResponseEntity(HttpStatus.BAD_REQUEST, messages, request)
    }

    private fun buildResponseEntity(
        status: HttpStatus,
        messages: List<String?>,
        request: HttpServletRequest
    ): ResponseEntity<ErrorMessage> {
        val errorMessage = ErrorMessage(
            Instant.now(), status.value(), status.reasonPhrase, messages, request.requestURI
        )
        return ResponseEntity(errorMessage, status)
    }
}
