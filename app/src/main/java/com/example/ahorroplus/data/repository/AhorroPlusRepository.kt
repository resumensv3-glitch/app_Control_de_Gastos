package com.example.ahorroplus.data.repository

import com.example.ahorroplus.data.dao.CategoriaDao
import com.example.ahorroplus.data.dao.TransaccionDao
import com.example.ahorroplus.data.model.Categoria
import com.example.ahorroplus.data.model.TipoTransaccion
import com.example.ahorroplus.data.model.Transaccion
import kotlinx.coroutines.flow.Flow

class AhorroPlusRepository(
    private val transaccionDao: TransaccionDao,
    private val categoriaDao: CategoriaDao
) {
    // Transacciones
    fun getAllTransacciones(): Flow<List<Transaccion>> = transaccionDao.getAllTransacciones()
    
    fun getTransaccionesByTipo(tipo: TipoTransaccion): Flow<List<Transaccion>> =
        transaccionDao.getTransaccionesByTipo(tipo)
    
    fun getTransaccionesByCategoria(categoriaId: Long): Flow<List<Transaccion>> =
        transaccionDao.getTransaccionesByCategoria(categoriaId)
    
    fun getTotalIngresos(): Flow<Double?> = transaccionDao.getTotalIngresos()
    
    fun getTotalEgresos(): Flow<Double?> = transaccionDao.getTotalEgresos()
    
    suspend fun insertTransaccion(transaccion: Transaccion): Long =
        transaccionDao.insertTransaccion(transaccion)
    
    suspend fun updateTransaccion(transaccion: Transaccion) =
        transaccionDao.updateTransaccion(transaccion)
    
    suspend fun deleteTransaccion(transaccion: Transaccion) =
        transaccionDao.deleteTransaccion(transaccion)
    
    // Categorías
    fun getAllCategorias(): Flow<List<Categoria>> = categoriaDao.getAllCategorias()
    
    suspend fun getCategoriaById(id: Long): Categoria? = categoriaDao.getCategoriaById(id)
    
    suspend fun insertCategoria(categoria: Categoria): Long =
        categoriaDao.insertCategoria(categoria)
    
    suspend fun updateCategoria(categoria: Categoria) =
        categoriaDao.updateCategoria(categoria)
    
    suspend fun deleteCategoria(categoria: Categoria) =
        categoriaDao.deleteCategoria(categoria)
}




