package com.matefoldes.exchangerates.service.rates.latest

import com.matefoldes.exchangerates.domain.LatestRates
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File

private val LOG = LoggerFactory.getLogger(LatestRatesClasspath::class.java)

@Component
class LatestRatesClasspath(
    @Value("\${rates.path.latest}") private val latestRatesPath: String
) {

    private val json = Json { isLenient = true }

    fun loadFromClasspath(): LatestRates {
        val classpathLatestRates: LatestRates = loadLatestRates()!!
        LOG.info(
            "Latest rates loaded from classpath: date={}, timestamp={}",
            classpathLatestRates.date,
            classpathLatestRates.timestamp
        )
        return classpathLatestRates
    }

    fun saveLatestRates(latestRates: LatestRates) = run {
        File(latestRatesPath).printWriter().use { out -> out.println(json.encodeToString(latestRates)) }
    }

    private fun loadLatestRates(): LatestRates? =
        if (File(latestRatesPath).exists()) {
            json.decodeFromString(File(latestRatesPath).readText())
        } else {
            LOG.error("Couldn't find LatestRates on classpath at $latestRatesPath")
            null
        }
}
