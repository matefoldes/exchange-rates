package com.matefoldes.exchangerates.api.domain

data class HistoricalRatesResponse(
    val date: String,
    val timestamp: String,
    val base: String,
    val rates: Map<String, String>
)
