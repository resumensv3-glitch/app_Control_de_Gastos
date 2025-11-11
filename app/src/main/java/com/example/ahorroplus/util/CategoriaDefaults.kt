package com.example.ahorroplus.util

import androidx.compose.ui.graphics.Color
import com.example.ahorroplus.data.model.Categoria
import com.example.ahorroplus.ui.theme.*

object CategoriaDefaults {
    val categoriasPorDefecto = listOf(
        Categoria(nombre = "Comida", icono = "restaurant", color = PrimaryOrange.toHex()),
        Categoria(nombre = "Transporte", icono = "directions_car", color = PrimaryBlue.toHex()),
        Categoria(nombre = "Entretenimiento", icono = "movie", color = PrimaryPurple.toHex()),
        Categoria(nombre = "Salud", icono = "local_hospital", color = PrimaryRed.toHex()),
        Categoria(nombre = "Compras", icono = "shopping_cart", color = PrimaryPink.toHex()),
        Categoria(nombre = "Educación", icono = "school", color = PrimaryIndigo.toHex()),
        Categoria(nombre = "Servicios", icono = "build", color = PrimaryGrey.toHex()),
        Categoria(nombre = "Salario", icono = "account_balance", color = PrimaryGreen.toHex()),
        Categoria(nombre = "Freelance", icono = "work", color = PrimaryTeal.toHex()),
        Categoria(nombre = "Otros", icono = "category", color = PrimaryBlueGrey.toHex())
    )
    
    private fun Color.toHex(): String {
        val red = (this.red * 255).toInt()
        val green = (this.green * 255).toInt()
        val blue = (this.blue * 255).toInt()
        return String.format("#%02X%02X%02X", red, green, blue)
    }
}

