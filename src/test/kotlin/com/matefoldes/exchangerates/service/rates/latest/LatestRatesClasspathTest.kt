package com.matefoldes.exchangerates.service.rates.latest

import com.matefoldes.exchangerates.domain.HistoricalRates
import com.matefoldes.exchangerates.domain.LatestRates
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File

private const val LATEST_RATES_PATH = "src/test/resources/rates/latestRates.json"
private const val LATEST_RATES_PATH_BACKUP = "src/test/resources/rates/backup/latestRates.json"

private const val TIMESTAMP = "1577923199"
private const val BASE = "EUR"
private const val DATE_VALID = "2020-01-01"

class LatestRatesClasspathTest {

    private val underTest = LatestRatesClasspath(LATEST_RATES_PATH)

    @BeforeEach
    fun `cleanUp file before tests`() {
        cleanUpFile()
    }

    @AfterEach
    fun `cleanUp file after test`() {
        cleanUpFile()
    }

    @Test
    fun `test loadFromClasspath should work properly`() {
        // GIVEN in setup
        createFile()
        val expected = createLatestRates()

        // WHEN
        val actual = underTest.loadFromClasspath()

        // THEN
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `test saveLatestRates should save properly`() {
        // GIVEN
        val expected = createLatestRates()

        // THEN
        underTest.saveLatestRates(expected)

        // WHEN
        val actual: LatestRates = underTest.loadFromClasspath()
        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `test loadFromClasspath should throw exception when file not found`() {
        // GIVEN
        val expected = emptyList<HistoricalRates>()

        // THEN
        assertThrows<NullPointerException> {
            // WHEN
            underTest.loadFromClasspath()
        }
    }

    private fun createFile() {
        if (!File(LATEST_RATES_PATH).exists()) {
            File(LATEST_RATES_PATH_BACKUP).copyTo(File(LATEST_RATES_PATH))
        }
    }

    private fun cleanUpFile() {
        File(LATEST_RATES_PATH).delete()
    }

    private fun createLatestRates() =
        LatestRates(
            isSuccess = true,
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
