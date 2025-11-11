package com.example.ahorroplus.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ahorroplus.data.model.Transaccion
import com.example.ahorroplus.data.repository.AhorroPlusRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: AhorroPlusRepository) : ViewModel() {
    
    val transacciones: StateFlow<List<Transaccion>> = repository.getAllTransacciones()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    val totalIngresos: StateFlow<Double> = repository.getTotalIngresos()
        .map { it ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)
    
    val totalEgresos: StateFlow<Double> = repository.getTotalEgresos()
        .map { it ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)
    
    val saldo: StateFlow<Double> = combine(totalIngresos, totalEgresos) { ingresos, egresos ->
        ingresos - egresos
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)
    
    fun deleteTransaccion(transaccion: Transaccion) {
        viewModelScope.launch {
            repository.deleteTransaccion(transaccion)
        }
    }
    
    companion object {
        fun provideFactory(repository: AhorroPlusRepository): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return HomeViewModel(repository) as T
                }
            }
        }
    }
}

