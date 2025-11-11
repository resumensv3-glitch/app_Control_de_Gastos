package com.example.ahorroplus.data.dao

import androidx.room.*
import com.example.ahorroplus.data.model.Categoria
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriaDao {
    @Query("SELECT * FROM categorias ORDER BY nombre ASC")
    fun getAllCategorias(): Flow<List<Categoria>>
    
    @Query("SELECT * FROM categorias WHERE id = :id")
    suspend fun getCategoriaById(id: Long): Categoria?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategoria(categoria: Categoria): Long
    
    @Update
    suspend fun updateCategoria(categoria: Categoria)
    
    @Delete
    suspend fun deleteCategoria(categoria: Categoria)
}




