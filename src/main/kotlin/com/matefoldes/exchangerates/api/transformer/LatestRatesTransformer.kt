package com.matefoldes.exchangerates.api.transformer

import com.matefoldes.exchangerates.api.domain.LatestRatesResponse
import com.matefoldes.exchangerates.domain.LatestRates
import org.springframework.stereotype.Component

@Component
class LatestRatesTransformer {
    fun transformToResponse(latestRates: LatestRates): LatestRatesResponse =
        LatestRatesResponse(
            date = latestRates.date,
            timestamp = latestRates.timestamp,
            base = latestRates.base,
            rates = latestRates.rates
        )
}
