package com.matefoldes.exchangerates.service.rates.historical

import com.matefoldes.exchangerates.domain.HistoricalRates
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.File

private val LOG = LoggerFactory.getLogger(HistoricalRatesClasspath::class.java)

@Component
class HistoricalRatesClasspath(
    @Value("\${rates.path.historical}") private val historicalRatesPath: String
) {

    private val json = Json { isLenient = true }

    fun getHistoricalRates(): MutableList<HistoricalRates> {
        val classpathHistoricalRates: MutableList<HistoricalRates> = loadHistoricalRates()
        LOG.info(
            "{} historical rates loaded from classpath",
            classpathHistoricalRates.size
        )
        return classpathHistoricalRates
    }

    fun saveHistoricalRates(historicalRates: List<HistoricalRates>) {
        File(historicalRatesPath).printWriter().use { out -> out.println(Json.encodeToString(historicalRates)) }
    }

    private fun loadHistoricalRates(): MutableList<HistoricalRates> =
        if (File(historicalRatesPath).exists()) {
            json.decodeFromString(File(historicalRatesPath).readText())
        } else {
            mutableListOf()
        }
}
