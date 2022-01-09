package com.matefoldes.exchangerates.service.rates.historical

import com.matefoldes.exchangerates.domain.HistoricalRates
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

private const val HISTORICAL_RATES_PATH = "src/test/resources/rates/historicalRates.json"
private const val HISTORICAL_RATES_PATH_BACKUP = "src/test/resources/rates/backup/historicalRates.json"

private const val TIMESTAMP = "1577923199"
private const val BASE = "EUR"
private const val DATE_VALID = "2020-01-01"

class HistoricalRatesClasspathTest {

    private val underTest = HistoricalRatesClasspath(HISTORICAL_RATES_PATH)

    @BeforeEach
    fun `cleanUp file before tests`() {
        cleanUpFile()
    }

    @AfterEach
    fun `cleanUp file after test`() {
        cleanUpFile()
    }

    @Test
    fun `test getHistoricalRates should work properly`() {
        // GIVEN in setup
        createFile()
        val expected = mutableListOf(createHistoricalRates())

        // WHEN
        val actual = underTest.getHistoricalRates()

        // THEN
        assertEquals(expected, actual)
    }

    @Test
    fun `test saveHistoricalRates should save properly`() {
        // GIVEN
        val expected = listOf(createHistoricalRates())

        // THEN
        underTest.saveHistoricalRates(expected)

        // WHEN
        val actual: List<HistoricalRates> = underTest.getHistoricalRates()
        assertEquals(expected, actual)
    }

    @Test
    fun `test getHistoricalRates should return empty list when file not found`() {
        // GIVEN
        val expected = emptyList<HistoricalRates>()

        // WHEN
        val actual = underTest.getHistoricalRates()

        // THEN
        assertEquals(expected, actual)
    }

    private fun createFile() {
        if (!File(HISTORICAL_RATES_PATH).exists()) {
            File(HISTORICAL_RATES_PATH_BACKUP).copyTo(File(HISTORICAL_RATES_PATH))
        }
    }

    private fun cleanUpFile() {
        File(HISTORICAL_RATES_PATH).delete()
    }

    private fun createHistoricalRates() =
        HistoricalRates(
            isSuccess = true,
            isHistorical = true,
            timestamp = TIMESTAMP,
            base = BASE,
            date = DATE_VALID,
            rates = createRates()
        )

    private fun createRates(): Map<String, String> =
        mapOf(
            "EUR" to "1",
            "USD" to "1.2",
            "GBP" to "1.3"
        )
}
