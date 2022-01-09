package com.matefoldes.exchangerates

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class ExchangeRatesApplication

fun main(args: Array<String>) {
    runApplication<ExchangeRatesApplication>(*args)
}
