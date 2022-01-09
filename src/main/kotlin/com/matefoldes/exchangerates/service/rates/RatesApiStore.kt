package com.matefoldes.exchangerates.service.rates

import com.matefoldes.exchangerates.domain.HistoricalRates
import com.matefoldes.exchangerates.domain.LatestRates
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject

private val LOG = LoggerFactory.getLogger(RatesApiStore::class.java)

@Component
class RatesApiStore(
    private val restTemplate: RestTemplate,
    @Value("\${exchangeratesapi.url}") private val apiUrl: String,
    @Value("\${exchangeratesapi.api.key}") private val apiKey: String,
) {

    private val json = Json { isLenient = true }

    fun getLatestRates(): LatestRates? {
        val url = "$apiUrl/latest?access_key=$apiKey"
        return try {
            val latestRates: LatestRates = json.decodeFromString(restTemplate.getForObject(url, String))
            if (latestRates.isSuccess) latestRates else null
        } catch (e: Exception) {
            LOG.error("Fetching from $url failed", e)
            null
        }
    }

    fun getHistoricalRates(date: String): HistoricalRates? {
        val url = "$apiUrl/$date?access_key=$apiKey"
        return try {
            val historicalRates: HistoricalRates = json.decodeFromString(restTemplate.getForObject(url, String))
            if (historicalRates.isSuccess) historicalRates else null
        } catch (e: Exception) {
            LOG.error("Fetching from $url failed", e)
            null
        }
    }
}
