package com.example.ahorroplus.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ahorroplus.data.dao.CategoriaDao
import com.example.ahorroplus.data.dao.TransaccionDao
import com.example.ahorroplus.data.model.Categoria
import com.example.ahorroplus.data.model.Transaccion

@Database(
    entities = [Transaccion::class, Categoria::class],
    version = 1,
    exportSchema = false
)
abstract class AhorroPlusDatabase : RoomDatabase() {
    abstract fun transaccionDao(): TransaccionDao
    abstract fun categoriaDao(): CategoriaDao
    
    companion object {
        const val DATABASE_NAME = "ahorro_plus_database"
    }
}







