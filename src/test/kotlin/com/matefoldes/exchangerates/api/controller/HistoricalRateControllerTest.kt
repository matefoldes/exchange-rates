package com.matefoldes.exchangerates.api.controller

import com.matefoldes.exchangerates.api.domain.HistoricalRatesResponse
import com.matefoldes.exchangerates.api.transformer.HistoricalRateTransformer
import com.matefoldes.exchangerates.domain.HistoricalRates
import com.matefoldes.exchangerates.exception.HistoricalRatesNotFoundException
import com.matefoldes.exchangerates.service.rates.historical.HistoricalRatesStore
import io.mockk.called
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith

private const val TIMESTAMP = "timestamp"
private const val BASE = "base"
private const val DATE = "2020-01-01"

@ExtendWith(MockKExtension::class)
class HistoricalRateControllerTest {

    @MockK
    private lateinit var historicalRatesStore: HistoricalRatesStore

    @MockK
    private lateinit var historicalRateTransformer: HistoricalRateTransformer

    @InjectMockKs
    private lateinit var underTest: HistoricalRateController

    @Test
    fun `test getHistoricalRates with valid date`() {
        // GIVEN
        val historicalRates = createHistoricalRates()
        val historicalRatesResponse = createHistoricalRatesResponse()
        every { historicalRatesStore.getHistoricalRatesByDate(DATE) } returns historicalRates
        every { historicalRateTransformer.transformToResponse(historicalRates) } returns historicalRatesResponse

        // WHEN
        val actual = underTest.getHistoricalRates(DATE)

        // THEN
        assert(actual == historicalRatesResponse)
        verify(exactly = 1) {
            historicalRatesStore.getHistoricalRatesByDate(DATE)
            historicalRateTransformer.transformToResponse(historicalRates)
        }
    }

    @Test
    fun `test getHistoricalRates with invalid date`() {
        // GIVEN
        val date = "invalid"
        every { historicalRatesStore.getHistoricalRatesByDate(date) } throws HistoricalRatesNotFoundException("not found")

        // THEN
        assertThrows<HistoricalRatesNotFoundException> {
            // WHEN
            underTest.getHistoricalRates(date)
        }
        verify(exactly = 1) { historicalRatesStore.getHistoricalRatesByDate(date) }
        verify { historicalRateTransformer wasNot called }
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
