package com.example.ahorroplus.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ahorroplus.data.model.TipoTransaccion
import com.example.ahorroplus.data.model.Transaccion
import com.example.ahorroplus.data.repository.AhorroPlusRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: AhorroPlusRepository) : ViewModel() {
    
    // Estados de filtros
    private val _tipoFiltro = MutableStateFlow<TipoTransaccion?>(null)
    val tipoFiltro: StateFlow<TipoTransaccion?> = _tipoFiltro.asStateFlow()
    
    private val _categoriaFiltro = MutableStateFlow<Long?>(null)
    val categoriaFiltro: StateFlow<Long?> = _categoriaFiltro.asStateFlow()
    
    private val _fechaInicio = MutableStateFlow<Long?>(null)
    val fechaInicio: StateFlow<Long?> = _fechaInicio.asStateFlow()
    
    private val _fechaFin = MutableStateFlow<Long?>(null)
    val fechaFin: StateFlow<Long?> = _fechaFin.asStateFlow()
    
    // Transacciones filtradas
    val transacciones: StateFlow<List<Transaccion>> = combine(
        _tipoFiltro,
        _categoriaFiltro,
        _fechaInicio,
        _fechaFin
    ) { tipo, categoria, fechaInicio, fechaFin ->
        Triple(tipo, categoria, Pair(fechaInicio, fechaFin))
    }.flatMapLatest { (tipo, categoria, fechas) ->
        repository.getTransaccionesFiltradas(tipo, categoria, fechas.first, fechas.second)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    // Totales filtrados
    val totalIngresos: StateFlow<Double> = combine(
        _tipoFiltro,
        _categoriaFiltro,
        _fechaInicio,
        _fechaFin
    ) { tipo, categoria, fechaInicio, fechaFin ->
        Triple(tipo, categoria, Pair(fechaInicio, fechaFin))
    }.flatMapLatest { (tipo, categoria, fechas) ->
        // Si hay filtro de tipo y es EGRESO, retornar 0 para ingresos
        if (tipo == TipoTransaccion.EGRESO) {
            flowOf(0.0)
        } else {
            repository.getTotalIngresosFiltrados(tipo, categoria, fechas.first, fechas.second)
        }
    }.map { it ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)
    
    val totalEgresos: StateFlow<Double> = combine(
        _tipoFiltro,
        _categoriaFiltro,
        _fechaInicio,
        _fechaFin
    ) { tipo, categoria, fechaInicio, fechaFin ->
        Triple(tipo, categoria, Pair(fechaInicio, fechaFin))
    }.flatMapLatest { (tipo, categoria, fechas) ->
        // Si hay filtro de tipo y es INGRESO, retornar 0 para egresos
        if (tipo == TipoTransaccion.INGRESO) {
            flowOf(0.0)
        } else {
            repository.getTotalEgresosFiltrados(tipo, categoria, fechas.first, fechas.second)
        }
    }.map { it ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)
    
    val saldo: StateFlow<Double> = combine(totalIngresos, totalEgresos) { ingresos, egresos ->
        ingresos - egresos
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)
    
    // Categorías para el filtro
    val categorias = repository.getAllCategorias()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    fun setTipoFiltro(tipo: TipoTransaccion?) {
        _tipoFiltro.value = tipo
    }
    
    fun setCategoriaFiltro(categoriaId: Long?) {
        _categoriaFiltro.value = categoriaId
    }
    
    fun setFechaInicio(fecha: Long?) {
        _fechaInicio.value = fecha
        // Validar que fechaFin no sea anterior a fechaInicio
        _fechaFin.value?.let { fechaFin ->
            if (fecha != null && fechaFin < fecha) {
                _fechaFin.value = null // Limpiar fechaFin si es inválida
            }
        }
    }
    
    fun setFechaFin(fecha: Long?) {
        // Validar que fechaFin no sea anterior a fechaInicio
        _fechaInicio.value?.let { fechaInicio ->
            if (fecha != null && fecha < fechaInicio) {
                // No establecer fechaFin si es anterior a fechaInicio
                return
            }
        }
        _fechaFin.value = fecha
    }
    
    fun limpiarFiltros() {
        _tipoFiltro.value = null
        _categoriaFiltro.value = null
        _fechaInicio.value = null
        _fechaFin.value = null
    }
    
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

