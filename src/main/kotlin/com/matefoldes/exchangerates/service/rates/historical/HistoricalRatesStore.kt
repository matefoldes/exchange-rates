package com.matefoldes.exchangerates.service.rates.historical

import com.matefoldes.exchangerates.domain.HistoricalRates
import com.matefoldes.exchangerates.exception.HistoricalRatesNotFoundException
import com.matefoldes.exchangerates.exception.WrongDateFormatException
import com.matefoldes.exchangerates.service.rates.RatesApiStore
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val LOG = LoggerFactory.getLogger(HistoricalRatesStore::class.java)
private const val DATE_FORMAT_PATTERN = "YYYY-MM-dd"

@Component
class HistoricalRatesStore(
    private val ratesApiStore: RatesApiStore,
    private val historicalRatesClasspath: HistoricalRatesClasspath,
) {

    private val historicalRatesList: MutableList<HistoricalRates> = historicalRatesClasspath.getHistoricalRates()

    fun getHistoricalRatesByDate(date: String): HistoricalRates {
        val formattedDate = formatDate(date)
        return findHistoricalRates(formattedDate) ?: updateHistoricalRatesList(formattedDate)
    }

    private fun updateHistoricalRatesList(formattedDate: String): HistoricalRates {
        val historicalRatesFromApi = getHistoricalRatesFromApi(formattedDate)
        return if (historicalRatesFromApi != null) {
            saveRates(historicalRatesFromApi)
            historicalRatesFromApi
        } else {
            throw HistoricalRatesNotFoundException("$formattedDate not found, use $DATE_FORMAT_PATTERN format")
        }
    }

    private fun formatDate(date: String): String {
        return try {
            LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE).toString()
        } catch (ex: Exception) {
            LOG.warn("Wrong date format: $date", ex)
            throw WrongDateFormatException("Use $DATE_FORMAT_PATTERN for date, wrong date: $date")
        }
    }

    private fun getHistoricalRatesFromApi(date: String): HistoricalRates? {
        return try {
            ratesApiStore.getHistoricalRates(date)
        } catch (e: Exception) {
            LOG.error("Fetching from API failed", e)
            null
        }
    }

    private fun saveRates(newHistoricalRates: HistoricalRates) {
        historicalRatesList.add(newHistoricalRates)
        historicalRatesClasspath.saveHistoricalRates(historicalRatesList)
    }

    private fun findHistoricalRates(date: String): HistoricalRates? =
        historicalRatesList.find { it.date == date }
}
