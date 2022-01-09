package com.matefoldes.exchangerates.api.controller

import com.matefoldes.exchangerates.api.domain.LatestRatesResponse
import com.matefoldes.exchangerates.api.transformer.LatestRatesTransformer
import com.matefoldes.exchangerates.domain.LatestRates
import com.matefoldes.exchangerates.service.rates.latest.LatestRatesStore
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

private const val TIMESTAMP = "timestamp"
private const val BASE = "base"
private const val DATE = "date"

@ExtendWith(MockKExtension::class)
class LatestRateControllerTest {

    @MockK
    private lateinit var latestRatesStore: LatestRatesStore

    @MockK
    private lateinit var latestRatesTransformer: LatestRatesTransformer

    @InjectMockKs
    private lateinit var underTest: LatestRateController

    @Test
    fun `test getLatestRates should return properly`() {
        // GIVEN
        val latestRates = createLatestRates()
        val latestRatesResponse = createLatestRatesResponse()
        every { latestRatesStore.getCurrentLatestRates() } returns latestRates
        every { latestRatesTransformer.transformToResponse(latestRates) } returns latestRatesResponse

        // WHEN
        val actual = underTest.getLatestRates()

        // THEN
        assert(actual == latestRatesResponse)
    }

    private fun createLatestRates() =
        LatestRates(
            isSuccess = true,
            timestamp = TIMESTAMP,
            base = BASE,
            date = DATE,
            rates = createRates()
        )

    private fun createLatestRatesResponse() =
        LatestRatesResponse(
            timestamp = TIMESTAMP,
            base = BASE,
            date = DATE,
            rates = createRates()
        )

    private fun createRates() =
        mapOf(
            "EUR" to "1",
            "USD" to "2"
        )
}
