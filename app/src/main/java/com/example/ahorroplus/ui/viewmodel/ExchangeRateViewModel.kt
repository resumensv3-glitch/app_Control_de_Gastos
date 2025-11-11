package com.example.ahorroplus.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ahorroplus.api.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExchangeRateViewModel : ViewModel() {
    
    private val _usdToEur = MutableStateFlow<Double?>(null)
    val usdToEur: StateFlow<Double?> = _usdToEur.asStateFlow()
    
    private val _usdToMxn = MutableStateFlow<Double?>(null)
    val usdToMxn: StateFlow<Double?> = _usdToMxn.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    init {
        loadExchangeRates()
    }
    
    fun loadExchangeRates() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                // Usando API gratuita: exchangerate-api.com
                // No requiere API key para uso básico
                val response = ApiClient.exchangeRateApi.getLatestRates()
                
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    val rates = body.conversion_rates
                    rates?.let {
                        _usdToEur.value = it["EUR"]
                        _usdToMxn.value = it["MXN"]
                    }
                } else {
                    // Valores por defecto si la API falla
                    _usdToEur.value = 0.92 // Aproximado USD a EUR
                    _usdToMxn.value = 17.0 // Aproximado USD a MXN
                    _error.value = "Usando tasas aproximadas"
                }
            } catch (e: Exception) {
                // Valores por defecto en caso de error
                _usdToEur.value = 0.92
                _usdToMxn.value = 17.0
                _error.value = "Error al cargar tasas: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    companion object {
        fun provideFactory(): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ExchangeRateViewModel() as T
                }
            }
        }
    }
}

