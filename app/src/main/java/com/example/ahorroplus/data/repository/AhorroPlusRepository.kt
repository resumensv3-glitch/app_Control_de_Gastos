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
    
    // Métodos de filtrado
    fun getTransaccionesFiltradas(
        tipo: TipoTransaccion? = null,
        categoriaId: Long? = null,
        fechaInicio: Long? = null,
        fechaFin: Long? = null
    ): Flow<List<Transaccion>> {
        return when {
            // Casos con tipo, categoria y ambas fechas
            tipo != null && categoriaId != null && fechaInicio != null && fechaFin != null -> {
                transaccionDao.getTransaccionesByTipoCategoriaAndFecha(tipo, categoriaId, fechaInicio, fechaFin)
            }
            // Casos con tipo, categoria y solo fecha inicio
            tipo != null && categoriaId != null && fechaInicio != null -> {
                transaccionDao.getTransaccionesByTipoCategoriaDesdeFecha(tipo, categoriaId, fechaInicio)
            }
            // Casos con tipo, categoria y solo fecha fin
            tipo != null && categoriaId != null && fechaFin != null -> {
                transaccionDao.getTransaccionesByTipoCategoriaHastaFecha(tipo, categoriaId, fechaFin)
            }
            // Casos con tipo y ambas fechas
            tipo != null && fechaInicio != null && fechaFin != null -> {
                transaccionDao.getTransaccionesByTipoAndFecha(tipo, fechaInicio, fechaFin)
            }
            // Casos con tipo y solo fecha inicio
            tipo != null && fechaInicio != null -> {
                transaccionDao.getTransaccionesByTipoDesdeFecha(tipo, fechaInicio)
            }
            // Casos con tipo y solo fecha fin
            tipo != null && fechaFin != null -> {
                transaccionDao.getTransaccionesByTipoHastaFecha(tipo, fechaFin)
            }
            // Casos con tipo y categoria (sin fechas)
            tipo != null && categoriaId != null -> {
                transaccionDao.getTransaccionesByTipoAndCategoria(tipo, categoriaId)
            }
            // Casos con categoria y ambas fechas
            categoriaId != null && fechaInicio != null && fechaFin != null -> {
                transaccionDao.getTransaccionesByCategoriaAndFecha(categoriaId, fechaInicio, fechaFin)
            }
            // Casos con categoria y solo fecha inicio
            categoriaId != null && fechaInicio != null -> {
                transaccionDao.getTransaccionesByCategoriaDesdeFecha(categoriaId, fechaInicio)
            }
            // Casos con categoria y solo fecha fin
            categoriaId != null && fechaFin != null -> {
                transaccionDao.getTransaccionesByCategoriaHastaFecha(categoriaId, fechaFin)
            }
            // Casos con solo tipo
            tipo != null -> {
                transaccionDao.getTransaccionesByTipo(tipo)
            }
            // Casos con solo categoria
            categoriaId != null -> {
                transaccionDao.getTransaccionesByCategoria(categoriaId)
            }
            // Casos con ambas fechas (sin tipo ni categoria)
            fechaInicio != null && fechaFin != null -> {
                transaccionDao.getTransaccionesPorFecha(fechaInicio, fechaFin)
            }
            // Casos con solo fecha inicio
            fechaInicio != null -> {
                transaccionDao.getTransaccionesDesdeFecha(fechaInicio)
            }
            // Casos con solo fecha fin
            fechaFin != null -> {
                transaccionDao.getTransaccionesHastaFecha(fechaFin)
            }
            // Sin filtros
            else -> {
                transaccionDao.getAllTransacciones()
            }
        }
    }
    
    fun getTotalIngresosFiltrados(
        tipo: TipoTransaccion? = null,
        categoriaId: Long? = null,
        fechaInicio: Long? = null,
        fechaFin: Long? = null
    ): Flow<Double?> {
        // Si hay filtro de tipo, usar queries específicas por tipo
        val tipoFiltro = tipo ?: TipoTransaccion.INGRESO
        
        return when {
            // Casos con tipo, categoria y ambas fechas
            categoriaId != null && fechaInicio != null && fechaFin != null -> {
                transaccionDao.getTotalByTipoCategoriaAndFecha(tipoFiltro, categoriaId, fechaInicio, fechaFin)
            }
            // Casos con tipo, categoria y solo fecha inicio
            categoriaId != null && fechaInicio != null -> {
                transaccionDao.getTotalByTipoCategoriaDesdeFecha(tipoFiltro, categoriaId, fechaInicio)
            }
            // Casos con tipo, categoria y solo fecha fin
            categoriaId != null && fechaFin != null -> {
                transaccionDao.getTotalByTipoCategoriaHastaFecha(tipoFiltro, categoriaId, fechaFin)
            }
            // Casos con tipo y categoria (sin fechas)
            categoriaId != null -> {
                transaccionDao.getTotalByTipoAndCategoria(tipoFiltro, categoriaId)
            }
            // Casos con tipo y ambas fechas
            fechaInicio != null && fechaFin != null -> {
                transaccionDao.getTotalByTipoAndFecha(tipoFiltro, fechaInicio, fechaFin)
            }
            // Casos con tipo y solo fecha inicio
            fechaInicio != null -> {
                transaccionDao.getTotalByTipoDesdeFecha(tipoFiltro, fechaInicio)
            }
            // Casos con tipo y solo fecha fin
            fechaFin != null -> {
                transaccionDao.getTotalByTipoHastaFecha(tipoFiltro, fechaFin)
            }
            // Casos con solo tipo
            else -> {
                transaccionDao.getTotalByTipo(tipoFiltro)
            }
        }
    }
    
    fun getTotalEgresosFiltrados(
        tipo: TipoTransaccion? = null,
        categoriaId: Long? = null,
        fechaInicio: Long? = null,
        fechaFin: Long? = null
    ): Flow<Double?> {
        // Si hay filtro de tipo, usar queries específicas por tipo
        val tipoFiltro = tipo ?: TipoTransaccion.EGRESO
        
        return when {
            // Casos con tipo, categoria y ambas fechas
            categoriaId != null && fechaInicio != null && fechaFin != null -> {
                transaccionDao.getTotalByTipoCategoriaAndFecha(tipoFiltro, categoriaId, fechaInicio, fechaFin)
            }
            // Casos con tipo, categoria y solo fecha inicio
            categoriaId != null && fechaInicio != null -> {
                transaccionDao.getTotalByTipoCategoriaDesdeFecha(tipoFiltro, categoriaId, fechaInicio)
            }
            // Casos con tipo, categoria y solo fecha fin
            categoriaId != null && fechaFin != null -> {
                transaccionDao.getTotalByTipoCategoriaHastaFecha(tipoFiltro, categoriaId, fechaFin)
            }
            // Casos con tipo y categoria (sin fechas)
            categoriaId != null -> {
                transaccionDao.getTotalByTipoAndCategoria(tipoFiltro, categoriaId)
            }
            // Casos con tipo y ambas fechas
            fechaInicio != null && fechaFin != null -> {
                transaccionDao.getTotalByTipoAndFecha(tipoFiltro, fechaInicio, fechaFin)
            }
            // Casos con tipo y solo fecha inicio
            fechaInicio != null -> {
                transaccionDao.getTotalByTipoDesdeFecha(tipoFiltro, fechaInicio)
            }
            // Casos con tipo y solo fecha fin
            fechaFin != null -> {
                transaccionDao.getTotalByTipoHastaFecha(tipoFiltro, fechaFin)
            }
            // Casos con solo tipo
            else -> {
                transaccionDao.getTotalByTipo(tipoFiltro)
            }
        }
    }
    
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





