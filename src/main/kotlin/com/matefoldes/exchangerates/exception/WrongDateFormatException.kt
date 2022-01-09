package com.matefoldes.exchangerates.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class WrongDateFormatException(override val message: String) : RuntimeException(message)
