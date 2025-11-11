package com.example.ahorroplus.data.dao

import androidx.room.*
import com.example.ahorroplus.data.model.TipoTransaccion
import com.example.ahorroplus.data.model.Transaccion
import kotlinx.coroutines.flow.Flow

@Dao
interface TransaccionDao {
    @Query("SELECT * FROM transacciones ORDER BY fecha DESC")
    fun getAllTransacciones(): Flow<List<Transaccion>>
    
    @Query("SELECT * FROM transacciones WHERE tipo = :tipo ORDER BY fecha DESC")
    fun getTransaccionesByTipo(tipo: TipoTransaccion): Flow<List<Transaccion>>
    
    @Query("SELECT * FROM transacciones WHERE categoriaId = :categoriaId ORDER BY fecha DESC")
    fun getTransaccionesByCategoria(categoriaId: Long): Flow<List<Transaccion>>
    
    @Query("SELECT SUM(monto) FROM transacciones WHERE tipo = 'INGRESO'")
    fun getTotalIngresos(): Flow<Double?>
    
    @Query("SELECT SUM(monto) FROM transacciones WHERE tipo = 'EGRESO'")
    fun getTotalEgresos(): Flow<Double?>
    
    @Query("SELECT * FROM transacciones WHERE fecha BETWEEN :fechaInicio AND :fechaFin ORDER BY fecha DESC")
    fun getTransaccionesPorFecha(fechaInicio: Long, fechaFin: Long): Flow<List<Transaccion>>
    
    @Insert
    suspend fun insertTransaccion(transaccion: Transaccion): Long
    
    @Update
    suspend fun updateTransaccion(transaccion: Transaccion)
    
    @Delete
    suspend fun deleteTransaccion(transaccion: Transaccion)
}




