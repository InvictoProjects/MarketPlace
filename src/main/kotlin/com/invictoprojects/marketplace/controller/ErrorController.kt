package com.invictoprojects.marketplace.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ErrorController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(validException: MethodArgumentNotValidException): Map<String, String?>? {
        val firstError = validException.bindingResult.allErrors.first()
        val errorMessage = firstError.defaultMessage
        return mapOf("error" to errorMessage)
    }

}
