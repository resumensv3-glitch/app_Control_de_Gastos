package com.example.ahorroplus.util

import com.example.ahorroplus.data.repository.AhorroPlusRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

object DatabaseInitializer {
    fun initializeDatabase(repository: AhorroPlusRepository) {
        CoroutineScope(Dispatchers.IO).launch {
            val categorias = repository.getAllCategorias().first()
            
            if (categorias.isEmpty()) {
                CategoriaDefaults.categoriasPorDefecto.forEach { categoria ->
                    repository.insertCategoria(categoria)
                }
            }
        }
    }
}

