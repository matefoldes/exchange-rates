package com.matefoldes.exchangerates.api.controller

import com.matefoldes.exchangerates.api.domain.HistoricalRatesResponse
import com.matefoldes.exchangerates.api.transformer.HistoricalRateTransformer
import com.matefoldes.exchangerates.service.rates.historical.HistoricalRatesStore
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

private val LOG = LoggerFactory.getLogger(HistoricalRateController::class.java)

@RestController
class HistoricalRateController(
    private val historicalRatesStore: HistoricalRatesStore,
    private val historicalRateTransformer: HistoricalRateTransformer
) {
    @GetMapping("/historicalRates/{date}")
    fun getHistoricalRates(@PathVariable date: String): HistoricalRatesResponse {
        val start = System.currentTimeMillis()

        val historicalRates = historicalRatesStore.getHistoricalRatesByDate(date)
        val response = historicalRateTransformer.transformToResponse(historicalRates)

        val end = System.currentTimeMillis() - start
        LOG.info(
            "/historicalRates/{} called, using historicalRates: date={}, timestamp={}, runtime={}",
            date,
            historicalRates.date,
            historicalRates.timestamp,
            end
        )
        return response
    }
}
