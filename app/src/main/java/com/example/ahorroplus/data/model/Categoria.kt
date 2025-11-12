package com.example.ahorroplus.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categorias")
data class Categoria(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nombre: String,
    val icono: String, // Nombre del icono (ej: "restaurant", "shopping", etc.)
    val color: String // Color en formato hex (ej: "#FF5722")
)








