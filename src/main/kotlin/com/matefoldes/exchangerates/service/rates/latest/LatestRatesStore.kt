package com.matefoldes.exchangerates.service.rates.latest

import com.matefoldes.exchangerates.domain.LatestRates
import com.matefoldes.exchangerates.domain.RatesLoadSource
import com.matefoldes.exchangerates.service.rates.RatesApiStore
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

private val LOG = LoggerFactory.getLogger(LatestRatesStore::class.java)

@Component
class LatestRatesStore(
    private val ratesApiStore: RatesApiStore,
    private val latestRatesClasspath: LatestRatesClasspath,
    @Value("\${exchangeratesapi.source}") private val ratesLoadSource: RatesLoadSource
) {
    private var latestRates: LatestRates = initLatestRates()

    fun getCurrentLatestRates() = latestRates

    fun updateLatestRatesFromApi() = run {
        if (ratesLoadSource == RatesLoadSource.API) {
            val freshLatestRates = ratesApiStore.getLatestRates()
            if (freshLatestRates != null) {
                LOG.warn(
                    "Updating and saving new latest rates. Old version: date={}, timestamp={}. New version: date={}, timestamp={}",
                    latestRates.date, latestRates,
                    freshLatestRates.date, freshLatestRates.timestamp
                )
                latestRatesClasspath.saveLatestRates(freshLatestRates)
                latestRates = freshLatestRates
            }
        }
    }

    private fun initLatestRates(): LatestRates {
        return if (ratesLoadSource == RatesLoadSource.API) {
            val latestRates: LatestRates? = ratesApiStore.getLatestRates()
            if (latestRates == null) {
                val classpathLatestRates = latestRatesClasspath.loadFromClasspath()
                LOG.warn(
                    "Init fetching latest rates failed, loading from classpath: date={}, timestamp={}",
                    classpathLatestRates.date,
                    classpathLatestRates.timestamp
                )
                classpathLatestRates
            } else {
                LOG.info("Init fetching done, with date={}, timestamp={}", latestRates.date, latestRates.timestamp)
                latestRates
            }
        } else {
            latestRatesClasspath.loadFromClasspath()
        }
    }
}
