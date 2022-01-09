package com.matefoldes.exchangerates.api.controller

import com.matefoldes.exchangerates.api.domain.LatestRatesResponse
import com.matefoldes.exchangerates.api.transformer.LatestRatesTransformer
import com.matefoldes.exchangerates.service.rates.latest.LatestRatesStore
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

private val LOG = LoggerFactory.getLogger(LatestRateController::class.java)

@RestController
class LatestRateController(
    private val latestRatesStore: LatestRatesStore,
    private val latestRatesTransformer: LatestRatesTransformer
) {
    @GetMapping("/latestRates")
    fun getLatestRates(): LatestRatesResponse {
        val start = System.currentTimeMillis()

        val latestRates = latestRatesStore.getCurrentLatestRates()
        val response = latestRatesTransformer.transformToResponse(latestRates)

        val end = System.currentTimeMillis() - start
        LOG.info(
            "/latestRates called, using latestRates: date={}, timestamp={}, runtime={}",
            latestRates.date,
            latestRates.timestamp,
            end
        )
        return response
    }
}
