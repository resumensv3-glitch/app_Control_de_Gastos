package com.example.ahorroplus.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ahorroplus.data.model.Categoria
import com.example.ahorroplus.data.repository.AhorroPlusRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CategoriaViewModel(private val repository: AhorroPlusRepository) : ViewModel() {
    
    val categorias: StateFlow<List<Categoria>> = repository.getAllCategorias()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    private val _nombre = MutableStateFlow("")
    val nombre: StateFlow<String> = _nombre.asStateFlow()
    
    private val _iconoSeleccionado = MutableStateFlow<String?>(null)
    val iconoSeleccionado: StateFlow<String?> = _iconoSeleccionado.asStateFlow()
    
    private val _colorSeleccionado = MutableStateFlow<String?>(null)
    val colorSeleccionado: StateFlow<String?> = _colorSeleccionado.asStateFlow()
    
    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()
    
    fun setNombre(value: String) {
        _nombre.value = value
    }
    
    fun setIcono(icono: String) {
        _iconoSeleccionado.value = icono
    }
    
    fun setColor(color: String) {
        _colorSeleccionado.value = color
    }
    
    fun limpiarMensaje() {
        _mensaje.value = null
    }
    
    fun guardarCategoria() {
        viewModelScope.launch {
            if (_nombre.value.isBlank()) {
                _mensaje.value = "Ingrese un nombre para la categoría"
                return@launch
            }
            
            if (_iconoSeleccionado.value == null) {
                _mensaje.value = "Seleccione un icono"
                return@launch
            }
            
            if (_colorSeleccionado.value == null) {
                _mensaje.value = "Seleccione un color"
                return@launch
            }
            
            val categoria = Categoria(
                nombre = _nombre.value,
                icono = _iconoSeleccionado.value!!,
                color = _colorSeleccionado.value!!
            )
            
            repository.insertCategoria(categoria)
            
            // Limpiar campos
            _nombre.value = ""
            _iconoSeleccionado.value = null
            _colorSeleccionado.value = null
            _mensaje.value = "Categoría guardada exitosamente"
        }
    }
    
    fun deleteCategoria(categoria: Categoria) {
        viewModelScope.launch {
            repository.deleteCategoria(categoria)
        }
    }
    
    companion object {
        fun provideFactory(repository: AhorroPlusRepository): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return CategoriaViewModel(repository) as T
                }
            }
        }
    }
}

