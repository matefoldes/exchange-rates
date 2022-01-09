package com.matefoldes.exchangerates.service.rates.latest

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

private const val ONE_HOUR = 3600000L
private const val TWELVE_HOURS = 12 * ONE_HOUR

@Component
class LatestRatesScheduledJob(
    private val latestRatesStore: LatestRatesStore
) {

    @Scheduled(fixedDelay = TWELVE_HOURS, initialDelay = TWELVE_HOURS)
    fun scheduledLatestRates() {
        latestRatesStore.updateLatestRatesFromApi()
    }
}
