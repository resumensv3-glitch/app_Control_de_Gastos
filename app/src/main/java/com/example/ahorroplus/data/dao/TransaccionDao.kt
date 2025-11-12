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
    
    @Query("SELECT * FROM transacciones WHERE tipo = :tipo AND fecha BETWEEN :fechaInicio AND :fechaFin ORDER BY fecha DESC")
    fun getTransaccionesByTipoAndFecha(tipo: TipoTransaccion, fechaInicio: Long, fechaFin: Long): Flow<List<Transaccion>>
    
    @Query("SELECT * FROM transacciones WHERE tipo = :tipo AND categoriaId = :categoriaId ORDER BY fecha DESC")
    fun getTransaccionesByTipoAndCategoria(tipo: TipoTransaccion, categoriaId: Long): Flow<List<Transaccion>>
    
    @Query("SELECT * FROM transacciones WHERE categoriaId = :categoriaId AND fecha BETWEEN :fechaInicio AND :fechaFin ORDER BY fecha DESC")
    fun getTransaccionesByCategoriaAndFecha(categoriaId: Long, fechaInicio: Long, fechaFin: Long): Flow<List<Transaccion>>
    
    @Query("SELECT * FROM transacciones WHERE tipo = :tipo AND categoriaId = :categoriaId AND fecha BETWEEN :fechaInicio AND :fechaFin ORDER BY fecha DESC")
    fun getTransaccionesByTipoCategoriaAndFecha(tipo: TipoTransaccion, categoriaId: Long, fechaInicio: Long, fechaFin: Long): Flow<List<Transaccion>>
    
    @Query("SELECT SUM(monto) FROM transacciones WHERE tipo = 'INGRESO' AND fecha BETWEEN :fechaInicio AND :fechaFin")
    fun getTotalIngresosPorFecha(fechaInicio: Long, fechaFin: Long): Flow<Double?>
    
    @Query("SELECT SUM(monto) FROM transacciones WHERE tipo = 'EGRESO' AND fecha BETWEEN :fechaInicio AND :fechaFin")
    fun getTotalEgresosPorFecha(fechaInicio: Long, fechaFin: Long): Flow<Double?>
    
    @Query("SELECT SUM(monto) FROM transacciones WHERE tipo = 'INGRESO' AND categoriaId = :categoriaId")
    fun getTotalIngresosPorCategoria(categoriaId: Long): Flow<Double?>
    
    @Query("SELECT SUM(monto) FROM transacciones WHERE tipo = 'EGRESO' AND categoriaId = :categoriaId")
    fun getTotalEgresosPorCategoria(categoriaId: Long): Flow<Double?>
    
    @Query("SELECT SUM(monto) FROM transacciones WHERE tipo = 'INGRESO' AND categoriaId = :categoriaId AND fecha BETWEEN :fechaInicio AND :fechaFin")
    fun getTotalIngresosPorCategoriaYFecha(categoriaId: Long, fechaInicio: Long, fechaFin: Long): Flow<Double?>
    
    @Query("SELECT SUM(monto) FROM transacciones WHERE tipo = 'EGRESO' AND categoriaId = :categoriaId AND fecha BETWEEN :fechaInicio AND :fechaFin")
    fun getTotalEgresosPorCategoriaYFecha(categoriaId: Long, fechaInicio: Long, fechaFin: Long): Flow<Double?>
    
    // Queries para filtrado por fecha única
    @Query("SELECT * FROM transacciones WHERE fecha >= :fechaInicio ORDER BY fecha DESC")
    fun getTransaccionesDesdeFecha(fechaInicio: Long): Flow<List<Transaccion>>
    
    @Query("SELECT * FROM transacciones WHERE fecha <= :fechaFin ORDER BY fecha DESC")
    fun getTransaccionesHastaFecha(fechaFin: Long): Flow<List<Transaccion>>
    
    @Query("SELECT * FROM transacciones WHERE tipo = :tipo AND fecha >= :fechaInicio ORDER BY fecha DESC")
    fun getTransaccionesByTipoDesdeFecha(tipo: TipoTransaccion, fechaInicio: Long): Flow<List<Transaccion>>
    
    @Query("SELECT * FROM transacciones WHERE tipo = :tipo AND fecha <= :fechaFin ORDER BY fecha DESC")
    fun getTransaccionesByTipoHastaFecha(tipo: TipoTransaccion, fechaFin: Long): Flow<List<Transaccion>>
    
    @Query("SELECT * FROM transacciones WHERE categoriaId = :categoriaId AND fecha >= :fechaInicio ORDER BY fecha DESC")
    fun getTransaccionesByCategoriaDesdeFecha(categoriaId: Long, fechaInicio: Long): Flow<List<Transaccion>>
    
    @Query("SELECT * FROM transacciones WHERE categoriaId = :categoriaId AND fecha <= :fechaFin ORDER BY fecha DESC")
    fun getTransaccionesByCategoriaHastaFecha(categoriaId: Long, fechaFin: Long): Flow<List<Transaccion>>
    
    @Query("SELECT * FROM transacciones WHERE tipo = :tipo AND categoriaId = :categoriaId AND fecha >= :fechaInicio ORDER BY fecha DESC")
    fun getTransaccionesByTipoCategoriaDesdeFecha(tipo: TipoTransaccion, categoriaId: Long, fechaInicio: Long): Flow<List<Transaccion>>
    
    @Query("SELECT * FROM transacciones WHERE tipo = :tipo AND categoriaId = :categoriaId AND fecha <= :fechaFin ORDER BY fecha DESC")
    fun getTransaccionesByTipoCategoriaHastaFecha(tipo: TipoTransaccion, categoriaId: Long, fechaFin: Long): Flow<List<Transaccion>>
    
    // Queries para totales filtrados por tipo
    @Query("SELECT SUM(monto) FROM transacciones WHERE tipo = :tipo")
    fun getTotalByTipo(tipo: TipoTransaccion): Flow<Double?>
    
    @Query("SELECT SUM(monto) FROM transacciones WHERE tipo = :tipo AND categoriaId = :categoriaId")
    fun getTotalByTipoAndCategoria(tipo: TipoTransaccion, categoriaId: Long): Flow<Double?>
    
    @Query("SELECT SUM(monto) FROM transacciones WHERE tipo = :tipo AND fecha BETWEEN :fechaInicio AND :fechaFin")
    fun getTotalByTipoAndFecha(tipo: TipoTransaccion, fechaInicio: Long, fechaFin: Long): Flow<Double?>
    
    @Query("SELECT SUM(monto) FROM transacciones WHERE tipo = :tipo AND categoriaId = :categoriaId AND fecha BETWEEN :fechaInicio AND :fechaFin")
    fun getTotalByTipoCategoriaAndFecha(tipo: TipoTransaccion, categoriaId: Long, fechaInicio: Long, fechaFin: Long): Flow<Double?>
    
    @Query("SELECT SUM(monto) FROM transacciones WHERE tipo = :tipo AND fecha >= :fechaInicio")
    fun getTotalByTipoDesdeFecha(tipo: TipoTransaccion, fechaInicio: Long): Flow<Double?>
    
    @Query("SELECT SUM(monto) FROM transacciones WHERE tipo = :tipo AND fecha <= :fechaFin")
    fun getTotalByTipoHastaFecha(tipo: TipoTransaccion, fechaFin: Long): Flow<Double?>
    
    @Query("SELECT SUM(monto) FROM transacciones WHERE tipo = :tipo AND categoriaId = :categoriaId AND fecha >= :fechaInicio")
    fun getTotalByTipoCategoriaDesdeFecha(tipo: TipoTransaccion, categoriaId: Long, fechaInicio: Long): Flow<Double?>
    
    @Query("SELECT SUM(monto) FROM transacciones WHERE tipo = :tipo AND categoriaId = :categoriaId AND fecha <= :fechaFin")
    fun getTotalByTipoCategoriaHastaFecha(tipo: TipoTransaccion, categoriaId: Long, fechaFin: Long): Flow<Double?>
    
    @Insert
    suspend fun insertTransaccion(transaccion: Transaccion): Long
    
    @Update
    suspend fun updateTransaccion(transaccion: Transaccion)
    
    @Delete
    suspend fun deleteTransaccion(transaccion: Transaccion)
}





