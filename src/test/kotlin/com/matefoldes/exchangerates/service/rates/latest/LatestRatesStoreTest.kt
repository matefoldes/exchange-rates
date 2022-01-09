package com.matefoldes.exchangerates.service.rates.latest

import com.matefoldes.exchangerates.domain.LatestRates
import com.matefoldes.exchangerates.domain.RatesLoadSource
import com.matefoldes.exchangerates.service.rates.RatesApiStore
import io.mockk.called
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

private const val TIMESTAMP = "timestamp"
private const val BASE = "base"
private const val DATE = "2020-01-01"
private const val DATE_NEW = "2020-01-02"

@ExtendWith(MockKExtension::class)
class LatestRatesStoreTest {

    @MockK
    private lateinit var ratesApiStore: RatesApiStore

    @MockK
    private lateinit var latestRatesClasspath: LatestRatesClasspath

    private lateinit var underTest: LatestRatesStore

    @Test
    fun `test getCurrentLatestRates returns properly when source is API`() {
        // GIVEN
        val expected = createLatestRates(DATE)
        every { ratesApiStore.getLatestRates() } returns expected
        underTest = LatestRatesStore(ratesApiStore, latestRatesClasspath, RatesLoadSource.API)

        // WHEN
        val actual = underTest.getCurrentLatestRates()

        // THEN
        assertEquals(expected, actual)
        verify(exactly = 1) { ratesApiStore.getLatestRates() }
        verify(exactly = 0) { ratesApiStore.getHistoricalRates(any()) }
        verify { latestRatesClasspath wasNot called }
    }

    @Test
    fun `test getCurrentLatestRates loads from classpath when source is API but answers null`() {
        // GIVEN
        val expected = createLatestRates(DATE)
        every { ratesApiStore.getLatestRates() } returns null
        every { latestRatesClasspath.loadFromClasspath() } returns expected
        underTest = LatestRatesStore(ratesApiStore, latestRatesClasspath, RatesLoadSource.API)

        // WHEN
        val actual = underTest.getCurrentLatestRates()

        // THEN
        assertEquals(expected, actual)
        verify(exactly = 1) {
            ratesApiStore.getLatestRates()
            latestRatesClasspath.loadFromClasspath()
        }
        verify(exactly = 0) {
            ratesApiStore.getHistoricalRates(any())
            latestRatesClasspath.saveLatestRates(any())
        }
    }

    @Test
    fun `test getCurrentLatestRates loads from classpath when source is CLASSPATH`() {
        // GIVEN
        val expected = createLatestRates(DATE)
        every { latestRatesClasspath.loadFromClasspath() } returns expected
        underTest = LatestRatesStore(ratesApiStore, latestRatesClasspath, RatesLoadSource.CLASSPATH)

        // WHEN
        val actual = underTest.getCurrentLatestRates()

        // THEN
        assertEquals(expected, actual)
        verify(exactly = 1) { latestRatesClasspath.loadFromClasspath() }
        verify(exactly = 0) { latestRatesClasspath.saveLatestRates(any()) }
        verify { ratesApiStore wasNot called }
    }

    @Test
    fun `test updateLatestRatesFromApi does not call api if source is CLASSPATH`() {
        // GIVEN
        every { latestRatesClasspath.loadFromClasspath() } returns createLatestRates(DATE)
        underTest = LatestRatesStore(ratesApiStore, latestRatesClasspath, RatesLoadSource.CLASSPATH)

        // WHEN
        underTest.updateLatestRatesFromApi()

        // THEN
        verify(exactly = 1) { latestRatesClasspath.loadFromClasspath() }
        verify(exactly = 0) { latestRatesClasspath.saveLatestRates(any()) }
        verify { ratesApiStore wasNot called }
    }

    @Test
    fun `test updateLatestRatesFromApi does not update if api returns null`() {
        // GIVEN
        val expected = createLatestRates(DATE)
        every { ratesApiStore.getLatestRates() } returns null
        every { latestRatesClasspath.loadFromClasspath() } returns expected
        underTest = LatestRatesStore(ratesApiStore, latestRatesClasspath, RatesLoadSource.API)

        // WHEN
        underTest.updateLatestRatesFromApi()

        // THEN
        verify(exactly = 2) { ratesApiStore.getLatestRates() }
        verify(exactly = 1) { latestRatesClasspath.loadFromClasspath() }
        verify(exactly = 0) {
            ratesApiStore.getHistoricalRates(any())
            latestRatesClasspath.saveLatestRates(any())
        }
    }

    @Test
    fun `test updateLatestRatesFromApi updates properly and saves new rates to classpath`() {
        // GIVEN
        val oldLatestRates = createLatestRates(DATE)
        val newLatestRates = createLatestRates(DATE_NEW)
        every { ratesApiStore.getLatestRates() } returns oldLatestRates
        underTest = LatestRatesStore(ratesApiStore, latestRatesClasspath, RatesLoadSource.API)
        every { ratesApiStore.getLatestRates() } returns newLatestRates
        every { latestRatesClasspath.saveLatestRates(newLatestRates) } just runs

        // WHEN
        underTest.updateLatestRatesFromApi()

        // THEN
        assertEquals(newLatestRates, underTest.getCurrentLatestRates())
        verify(exactly = 2) { ratesApiStore.getLatestRates() }
        verify(exactly = 1) { latestRatesClasspath.saveLatestRates(newLatestRates) }
        verify(exactly = 0) {
            ratesApiStore.getHistoricalRates(any())
            latestRatesClasspath.loadFromClasspath()
        }
    }

    private fun createLatestRates(date: String) =
        LatestRates(
            isSuccess = true,
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
