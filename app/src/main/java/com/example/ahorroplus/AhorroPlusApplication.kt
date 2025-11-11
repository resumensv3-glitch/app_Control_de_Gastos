package com.example.ahorroplus

import android.app.Application
import androidx.room.Room
import com.example.ahorroplus.data.database.AhorroPlusDatabase
import com.example.ahorroplus.data.repository.AhorroPlusRepository

class AhorroPlusApplication : Application() {
    val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            AhorroPlusDatabase::class.java,
            AhorroPlusDatabase.DATABASE_NAME
        ).build()
    }
    
    val repository by lazy {
        AhorroPlusRepository(
            database.transaccionDao(),
            database.categoriaDao()
        )
    }
}




