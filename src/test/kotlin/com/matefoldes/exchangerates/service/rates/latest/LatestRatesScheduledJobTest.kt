package com.matefoldes.exchangerates.service.rates.latest

import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class LatestRatesScheduledJobTest {

    @MockK
    private lateinit var latestRatesStore: LatestRatesStore

    @InjectMockKs
    private lateinit var underTest: LatestRatesScheduledJob

    @Test
    fun `test scheduledLatestRates should work properly`() {
        // GIVEN
        every { latestRatesStore.updateLatestRatesFromApi() } just Runs

        // WHEN
        underTest.scheduledLatestRates()

        // THEN
        verify(exactly = 1) { latestRatesStore.updateLatestRatesFromApi() }
        verify(exactly = 0) { latestRatesStore.getCurrentLatestRates() }
    }
}
