package com.example.ahorroplus.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "transacciones",
    foreignKeys = [
        ForeignKey(
            entity = Categoria::class,
            parentColumns = ["id"],
            childColumns = ["categoriaId"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class Transaccion(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val tipo: TipoTransaccion,
    val monto: Double,
    val descripcion: String,
    val categoriaId: Long?,
    val fecha: Long = System.currentTimeMillis()
)

enum class TipoTransaccion {
    INGRESO,
    EGRESO
}




