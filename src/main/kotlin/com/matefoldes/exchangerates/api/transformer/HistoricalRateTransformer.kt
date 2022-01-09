package com.matefoldes.exchangerates.api.transformer

import com.matefoldes.exchangerates.api.domain.HistoricalRatesResponse
import com.matefoldes.exchangerates.domain.HistoricalRates
import org.springframework.stereotype.Component

@Component
class HistoricalRateTransformer {
    fun transformToResponse(historicalRates: HistoricalRates): HistoricalRatesResponse =
        HistoricalRatesResponse(
            historicalRates.date,
            historicalRates.timestamp,
            historicalRates.base,
            historicalRates.rates
        )
}
