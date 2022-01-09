package com.matefoldes.exchangerates.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class HistoricalRatesNotFoundException(override val message: String) : RuntimeException(message)
