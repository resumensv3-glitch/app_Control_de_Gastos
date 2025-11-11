package com.example.ahorroplus.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ahorroplus.data.model.Categoria
import com.example.ahorroplus.data.model.TipoTransaccion
import com.example.ahorroplus.data.model.Transaccion
import com.example.ahorroplus.data.repository.AhorroPlusRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TransaccionViewModel(private val repository: AhorroPlusRepository) : ViewModel() {
    
    val categorias: StateFlow<List<Categoria>> = repository.getAllCategorias()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    private val _monto = MutableStateFlow("")
    val monto: StateFlow<String> = _monto.asStateFlow()
    
    private val _descripcion = MutableStateFlow("")
    val descripcion: StateFlow<String> = _descripcion.asStateFlow()
    
    private val _tipoSeleccionado = MutableStateFlow<TipoTransaccion?>(null)
    val tipoSeleccionado: StateFlow<TipoTransaccion?> = _tipoSeleccionado.asStateFlow()
    
    private val _categoriaSeleccionada = MutableStateFlow<Categoria?>(null)
    val categoriaSeleccionada: StateFlow<Categoria?> = _categoriaSeleccionada.asStateFlow()
    
    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()
    
    fun setMonto(value: String) {
        _monto.value = value
    }
    
    fun setDescripcion(value: String) {
        _descripcion.value = value
    }
    
    fun setTipo(tipo: TipoTransaccion) {
        _tipoSeleccionado.value = tipo
    }
    
    fun setCategoria(categoria: Categoria?) {
        _categoriaSeleccionada.value = categoria
    }
    
    fun limpiarMensaje() {
        _mensaje.value = null
    }
    
    fun resetearFormulario() {
        _monto.value = ""
        _descripcion.value = ""
        _tipoSeleccionado.value = null
        _categoriaSeleccionada.value = null
        _mensaje.value = null
    }
    
    fun guardarTransaccion() {
        viewModelScope.launch {
            val montoValue = _monto.value.toDoubleOrNull()
            val tipo = _tipoSeleccionado.value
            
            if (montoValue == null || montoValue <= 0) {
                _mensaje.value = "Ingrese un monto válido"
                return@launch
            }
            
            if (tipo == null) {
                _mensaje.value = "Seleccione un tipo de transacción"
                return@launch
            }
            
            val transaccion = Transaccion(
                tipo = tipo,
                monto = montoValue,
                descripcion = _descripcion.value.ifBlank { "Sin descripción" },
                categoriaId = _categoriaSeleccionada.value?.id
            )
            
            repository.insertTransaccion(transaccion)
            
            // Limpiar campos
            resetearFormulario()
            _mensaje.value = "Transacción guardada exitosamente"
        }
    }
    
    fun actualizarTransaccion(transaccion: Transaccion) {
        viewModelScope.launch {
            val montoValue = _monto.value.toDoubleOrNull()
            val tipo = _tipoSeleccionado.value
            
            if (montoValue == null || montoValue <= 0) {
                _mensaje.value = "Ingrese un monto válido"
                return@launch
            }
            
            if (tipo == null) {
                _mensaje.value = "Seleccione un tipo de transacción"
                return@launch
            }
            
            val transaccionActualizada = transaccion.copy(
                tipo = tipo,
                monto = montoValue,
                descripcion = _descripcion.value.ifBlank { "Sin descripción" },
                categoriaId = _categoriaSeleccionada.value?.id
            )
            
            repository.updateTransaccion(transaccionActualizada)
            _mensaje.value = "Transacción actualizada exitosamente"
        }
    }
    
    companion object {
        fun provideFactory(repository: AhorroPlusRepository): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return TransaccionViewModel(repository) as T
                }
            }
        }
    }
}

