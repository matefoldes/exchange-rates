package com.matefoldes.exchangerates.exception

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

private val LOG = LoggerFactory.getLogger(ExceptionHandlerController::class.java)

@ControllerAdvice
class ExceptionHandlerController : ResponseEntityExceptionHandler() {

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<Any> {
        LOG.error("Unexpected exception occurred", e)
        return ResponseEntity("Unexpected error.", HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
