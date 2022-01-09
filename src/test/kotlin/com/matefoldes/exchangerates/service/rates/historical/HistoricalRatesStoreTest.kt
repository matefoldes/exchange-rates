package com.matefoldes.exchangerates.service.rates.historical

import com.matefoldes.exchangerates.domain.HistoricalRates
import com.matefoldes.exchangerates.exception.HistoricalRatesNotFoundException
import com.matefoldes.exchangerates.exception.WrongDateFormatException
import com.matefoldes.exchangerates.service.rates.RatesApiStore
import io.mockk.Runs
import io.mockk.called
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith

private const val TIMESTAMP = "timestamp"
private const val BASE = "base"
private const val DATE_VALID = "2020-01-01"
private const val DATE_VALID_ANOTHER = "2020-01-02"
private const val DATE_INVALID = "invalid date"

@ExtendWith(MockKExtension::class)
class HistoricalRatesStoreTest {

    @MockK
    private lateinit var ratesApiStore: RatesApiStore

    @MockK
    private lateinit var historicalRatesClasspath: HistoricalRatesClasspath

    private lateinit var underTest: HistoricalRatesStore

    @BeforeEach
    fun setUp() {
        every { historicalRatesClasspath.getHistoricalRates() } returns mutableListOf(createHistoricalRates(DATE_VALID))
        underTest = HistoricalRatesStore(ratesApiStore, historicalRatesClasspath)
    }

    @Test
    fun `test getHistoricalRatesByDate with valid date should work properly`() {
        // GIVEN
        val expected = createHistoricalRates(DATE_VALID)
        every { historicalRatesClasspath.getHistoricalRates() } returns mutableListOf(expected)

        // WHEN
        val actual = underTest.getHistoricalRatesByDate(DATE_VALID)

        // THEN
        assertEquals(expected, actual)
        verify(exactly = 1) { historicalRatesClasspath.getHistoricalRates() }
        verify(exactly = 0) { historicalRatesClasspath.saveHistoricalRates(any()) }
        verify { ratesApiStore wasNot called }
    }

    @Test
    fun `test getHistoricalRatesByDate with invalid date should throw exception`() {
        // GIVEN in setUp
        // THEN
        assertThrows<WrongDateFormatException> {
            // WHEN
            underTest.getHistoricalRatesByDate(DATE_INVALID)
        }
    }

    @Test
    fun `test getHistoricalRatesByDate should call api and save it when not found locally`() {
        // GIVEN
        val expected = createHistoricalRates(DATE_VALID_ANOTHER)
        every { ratesApiStore.getHistoricalRates(DATE_VALID_ANOTHER) } returns expected
        every { historicalRatesClasspath.saveHistoricalRates(any()) } just Runs

        // WHEN
        val actual = underTest.getHistoricalRatesByDate(DATE_VALID_ANOTHER)

        // THEN
        assertEquals(expected, actual)
        verify(exactly = 1) {
            ratesApiStore.getHistoricalRates(DATE_VALID_ANOTHER)
            historicalRatesClasspath.saveHistoricalRates(any())
            historicalRatesClasspath.getHistoricalRates()
        }
    }

    @Test
    fun `test getHistoricalRatesByDate should throw exception when api answer is null`() {
        // GIVEN
        every { ratesApiStore.getHistoricalRates(DATE_VALID_ANOTHER) } throws Exception("no answer from api")

        // THEN
        assertThrows<HistoricalRatesNotFoundException> {
            // WHEN
            underTest.getHistoricalRatesByDate(DATE_VALID_ANOTHER)
        }

        // THEN
        verify(exactly = 1) {
            historicalRatesClasspath.getHistoricalRates()
            ratesApiStore.getHistoricalRates(DATE_VALID_ANOTHER)
        }
        verify(exactly = 0) { historicalRatesClasspath.saveHistoricalRates(any()) }
    }

    private fun createHistoricalRates(date: String) =
        HistoricalRates(
            isSuccess = true,
            isHistorical = true,
            timestamp = TIMESTAMP,
            base = BASE,
            date = date,
            rates = createRates()
        )

    private fun createRates(): Map<String, String> =
        mapOf(
            "EUR" to "1",
            "USD" to "2"
        )
}
