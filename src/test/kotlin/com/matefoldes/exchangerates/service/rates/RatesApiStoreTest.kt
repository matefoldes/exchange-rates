package com.matefoldes.exchangerates.service.rates

import com.matefoldes.exchangerates.domain.HistoricalRates
import com.matefoldes.exchangerates.domain.LatestRates
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject

private const val API_URL = "api_url"
private const val API_KEY = "api_key"
private const val DATE = "2020-01-01"
private const val TIMESTAMP = "1577923199"
private const val BASE = "EUR"
private const val LATEST_RATES_URL = "$API_URL/latest?access_key=$API_KEY"
private const val HISTORICAL_RATES_URL = "$API_URL/$DATE?access_key=$API_KEY"

@ExtendWith(MockKExtension::class)
class RatesApiStoreTest {

    @MockK
    private lateinit var restTemplate: RestTemplate

    private lateinit var underTest: RatesApiStore

    private val json = Json { isLenient = true }

    @BeforeEach
    fun setUp() {
        underTest = RatesApiStore(restTemplate, API_URL, API_KEY)
    }

    @Test
    fun `test getLatestRates should return properly`() {
        // GIVEN
        val expected = createLatestRates(true)
        val expectedAsJsonString = json.encodeToString(expected)
        every { restTemplate.getForObject(LATEST_RATES_URL, String) as String } returns expectedAsJsonString

        // WHEN
        val actual = underTest.getLatestRates()

        // THEN
        assertEquals(expected, actual)
        verify(exactly = 1) { restTemplate.getForObject(LATEST_RATES_URL, String) as String }
    }

    @Test
    fun `test getLatestRates should return null when restTemplate throws exception`() {
        // GIVEN
        every { restTemplate.getForObject(LATEST_RATES_URL, String) as String } throws Exception("no answer from api")


        // WHEN
        val actual = underTest.getLatestRates()

        // THEN
        assertNull(actual)
        verify(exactly = 1) { restTemplate.getForObject(LATEST_RATES_URL, String) as String }
    }

    @Test
    fun `test getLatestRates should return null when api answer isSuccess is false`() {
        // GIVEN
        val expected = createLatestRates(false)
        val expectedAsJsonString = json.encodeToString(expected)
        every { restTemplate.getForObject(LATEST_RATES_URL, String) as String } returns expectedAsJsonString

        // WHEN
        val actual = underTest.getLatestRates()

        // THEN
        assertNull(actual)
        verify(exactly = 1) { restTemplate.getForObject(LATEST_RATES_URL, String) as String }
    }

    @Test
    fun `test getHistoricalRates should return properly`() {
        // GIVEN
        val expected = createHistoricalRates(true)
        val expectedAsJsonString = json.encodeToString(expected)
        every { restTemplate.getForObject(HISTORICAL_RATES_URL, String) as String } returns expectedAsJsonString

        // WHEN
        val actual = underTest.getHistoricalRates(DATE)

        // THEN
        assertEquals(expected, actual)
        verify(exactly = 1) { restTemplate.getForObject(HISTORICAL_RATES_URL, String) as String }
    }

    @Test
    fun `test getHistoricalRates should return null when restTemplate throws exception`() {
        // GIVEN
        every { restTemplate.getForObject(HISTORICAL_RATES_URL, String) as String } throws Exception("no answer from api")

        // WHEN
        val actual = underTest.getHistoricalRates(DATE)

        // THEN
        assertNull(actual)
        verify(exactly = 1) { restTemplate.getForObject(HISTORICAL_RATES_URL, String) as String }
    }

    @Test
    fun `test getHistoricalRates should return null when api answer isSuccess is false`() {
        // GIVEN
        val expected = createHistoricalRates(false)
        val expectedAsJsonString = json.encodeToString(expected)
        every { restTemplate.getForObject(HISTORICAL_RATES_URL, String) as String } returns expectedAsJsonString

        // WHEN
        val actual = underTest.getHistoricalRates(DATE)

        // THEN
        assertNull(actual)
        verify(exactly = 1) { restTemplate.getForObject(HISTORICAL_RATES_URL, String) as String }
    }

    private fun createLatestRates(isSuccess: Boolean) =
        LatestRates(
            isSuccess = isSuccess,
            timestamp = TIMESTAMP,
            base = BASE,
            date = DATE,
            rates = createRates()
        )

    private fun createHistoricalRates(isSuccess: Boolean) =
        HistoricalRates(
            isSuccess = isSuccess,
            isHistorical = true,
            timestamp = TIMESTAMP,
            base = BASE,
            date = DATE,
            rates = createRates()
        )

    private fun createRates(): Map<String, String> =
        mapOf(
            "EUR" to "1",
            "USD" to "1.2",
            "GBP" to "1.3"
        )
}
