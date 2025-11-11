package com.example.ahorroplus.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    // API Base URL: exchangerate-api.com (gratuita, sin API key requerida)
    // Usando la versión gratuita que no requiere autenticación
    private const val BASE_URL = "https://api.exchangerate-api.com/v6/"
    
    // API utilizada: ExchangeRate-API (https://www.exchangerate-api.com/)
    // Es gratuita y no requiere API key para uso básico
    // Documentación: https://www.exchangerate-api.com/docs/free
    
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
    
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    val exchangeRateApi: ExchangeRateApi = retrofit.create(ExchangeRateApi::class.java)
}

