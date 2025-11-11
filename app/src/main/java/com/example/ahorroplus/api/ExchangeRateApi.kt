package com.example.ahorroplus.api

import retrofit2.Response
import retrofit2.http.GET


/**
 * API de tipo de cambio gratuita: exchangerate-api.com
 * No requiere API key para uso básico
 * Documentación: https://www.exchangerate-api.com/docs/free
 * 
 * Esta API proporciona tasas de cambio en tiempo real de forma gratuita
 */
interface ExchangeRateApi {
    @GET("latest/USD")
    suspend fun getLatestRates(): Response<ExchangeRateResponse>
}

data class ExchangeRateResponse(
    val result: String,
    val conversion_rates: Map<String, Double>?,
    val base_code: String?,
    val time_last_update_utc: String?
)


