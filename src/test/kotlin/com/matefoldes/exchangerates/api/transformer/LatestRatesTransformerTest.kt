package com.matefoldes.exchangerates.api.transformer

import com.matefoldes.exchangerates.api.domain.LatestRatesResponse
import com.matefoldes.exchangerates.domain.LatestRates
import org.junit.jupiter.api.Test

private const val TIMESTAMP = "timestamp"
private const val BASE = "base"
private const val DATE = "date"

class LatestRatesTransformerTest {

    private val underTest = LatestRatesTransformer()

    @Test
    fun `test transformToResponse should transform properly`() {
        // GIVEN
        val latestRates = createLatestRates()
        val latestRatesResponse = createLatestRatesResponse()

        // WHEN
        val actual = underTest.transformToResponse(latestRates)

        // THEN
        assert(latestRatesResponse == actual)
    }

    private fun createLatestRates(): LatestRates =
        LatestRates(
            isSuccess = true,
            timestamp = TIMESTAMP,
            base = BASE,
            date = DATE,
            rates = createRates()
        )

    private fun createLatestRatesResponse(): LatestRatesResponse =
        LatestRatesResponse(
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
