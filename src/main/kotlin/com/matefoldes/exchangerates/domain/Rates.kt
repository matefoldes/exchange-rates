package com.matefoldes.exchangerates.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LatestRates(
    @SerialName("success") val isSuccess: Boolean,
    val timestamp: String,
    val base: String,
    val date: String,
    val rates: Map<String, String>
)

@Serializable
data class HistoricalRates(
    @SerialName("success") val isSuccess: Boolean,
    @SerialName("historical") val isHistorical: Boolean,
    val date: String,
    val timestamp: String,
    val base: String,
    val rates: Map<String, String>
)
