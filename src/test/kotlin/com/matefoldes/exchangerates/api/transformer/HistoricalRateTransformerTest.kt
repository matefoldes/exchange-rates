package com.matefoldes.exchangerates.api.transformer

import com.matefoldes.exchangerates.api.domain.HistoricalRatesResponse
import com.matefoldes.exchangerates.domain.HistoricalRates
import org.junit.jupiter.api.Test

private const val TIMESTAMP = "timestamp"
private const val BASE = "base"
private const val DATE = "date"

class HistoricalRateTransformerTest {

    private val underTest = HistoricalRateTransformer()

    @Test
    fun `test transformToResponse should transform properly`() {
        // GIVEN
        val historicalRates = createHistoricalRates()
        val historicalRatesResponse = createHistoricalRatesResponse()

        // WHEN
        val actual = underTest.transformToResponse(historicalRates)

        // THEN
        assert(historicalRatesResponse == actual)
    }

    private fun createHistoricalRates(): HistoricalRates =
        HistoricalRates(
            isSuccess = true,
            isHistorical = true,
            timestamp = TIMESTAMP,
            base = BASE,
            date = DATE,
            rates = createRates()
        )

    private fun createHistoricalRatesResponse(): HistoricalRatesResponse =
        HistoricalRatesResponse(
            timestamp = TIMESTAMP,
            base = BASE,
            date = DATE,
            rates = createRates()
        )

    private fun createRates(): Map<String, String> =
        mapOf(
            "EUR" to "1",
            "USD" to "2"
        )
}
