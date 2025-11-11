package com.example.ahorroplus.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.ahorroplus.data.model.Categoria
import com.example.ahorroplus.data.model.TipoTransaccion
import com.example.ahorroplus.ui.viewmodel.TransaccionViewModel
import kotlinx.coroutines.delay
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    viewModel: TransaccionViewModel,
    onBack: () -> Unit
) {
    val monto by viewModel.monto.collectAsState()
    val descripcion by viewModel.descripcion.collectAsState()
    val tipoSeleccionado by viewModel.tipoSeleccionado.collectAsState()
    val categoriaSeleccionada by viewModel.categoriaSeleccionada.collectAsState()
    val categorias by viewModel.categorias.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()

    // Efecto para navegar de vuelta después de guardar exitosamente
    LaunchedEffect(key1 = mensaje) {
        if (mensaje?.contains("exitosamente") == true) {
            delay(1000) // Reducido a 1 segundo para mejor UX
            viewModel.resetearFormulario()
            onBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva Transacción", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Tipo de transacción
            Text(
                text = "Tipo de Transacción",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TipoButton(
                    tipo = TipoTransaccion.INGRESO,
                    isSelected = tipoSeleccionado == TipoTransaccion.INGRESO,
                    onClick = { viewModel.setTipo(TipoTransaccion.INGRESO) },
                    modifier = Modifier.weight(1f)
                )
                TipoButton(
                    tipo = TipoTransaccion.EGRESO,
                    isSelected = tipoSeleccionado == TipoTransaccion.EGRESO,
                    onClick = { viewModel.setTipo(TipoTransaccion.EGRESO) },
                    modifier = Modifier.weight(1f)
                )
            }

            // Campo de monto
            OutlinedTextField(
                value = monto,
                onValueChange = { viewModel.setMonto(it) },
                label = { Text("Monto") },
                leadingIcon = { Icon(Icons.Default.AttachMoney, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            // Campo de descripción
            OutlinedTextField(
                value = descripcion,
                onValueChange = { viewModel.setDescripcion(it) },
                label = { Text("Descripción") },
                leadingIcon = { Icon(Icons.Default.Description, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Categorías
            Text(
                text = "Categoría (Opcional)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            if (categorias.isEmpty()) {
                Text(
                    text = "No hay categorías disponibles. Crea una desde el menú de categorías.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        CategoryChip(
                            categoria = null,
                            isSelected = categoriaSeleccionada == null,
                            onClick = { viewModel.setCategoria(null) }
                        )
                    }
                    items(categorias) { categoria ->
                        CategoryChip(
                            categoria = categoria,
                            isSelected = categoriaSeleccionada?.id == categoria.id,
                            onClick = { viewModel.setCategoria(categoria) }
                        )
                    }
                }
            }

            // Mensaje
            mensaje?.let {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (it.contains("exitosamente")) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.errorContainer
                        }
                    )
                ) {
                    Text(
                        text = it,
                        modifier = Modifier.padding(16.dp),
                        color = if (it.contains("exitosamente")) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            MaterialTheme.colorScheme.onErrorContainer
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Botón guardar
            Button(
                onClick = { viewModel.guardarTransaccion() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = tipoSeleccionado != null
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Guardar Transacción", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
fun TipoButton(
    tipo: TipoTransaccion,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (text, icon, color) = when (tipo) {
        TipoTransaccion.INGRESO -> Triple("Ingreso", Icons.Default.ArrowUpward, Color(0xFF4CAF50))
        TipoTransaccion.EGRESO -> Triple("Egreso", Icons.Default.ArrowDownward, Color(0xFFF44336))
    }

    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = { Text(text) },
        leadingIcon = {
            Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp))
        },
        modifier = modifier,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = color.copy(alpha = 0.2f),
            selectedLabelColor = color
        )
    )
}

@Composable
fun CategoryChip(
    categoria: Categoria?,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val color = if (categoria != null && categoria.color.matches(Regex("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{8})$"))) {
        Color(android.graphics.Color.parseColor(categoria.color))
    } else {
        MaterialTheme.colorScheme.primary
    }

    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = { Text(categoria?.nombre ?: "Sin categoría") },
        leadingIcon = if (categoria != null && categoria.icono.isNotBlank()) {
            {
                Icon(getIconByName(categoria.icono), contentDescription = null, modifier = Modifier.size(20.dp))
            }
        } else null,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = color.copy(alpha = 0.2f),
            selectedLabelColor = color
        )
    )
}

@Composable
fun getIconByName(name: String): androidx.compose.ui.graphics.vector.ImageVector {
    if (name.isBlank()) return Icons.Default.Category
    return when (name.lowercase(Locale.getDefault())) {
        "restaurant" -> Icons.Default.Restaurant
        "directions_car" -> Icons.Default.DirectionsCar
        "movie" -> Icons.Default.Movie
        "local_hospital" -> Icons.Default.LocalHospital
        "shopping_cart" -> Icons.Default.ShoppingCart
        "school" -> Icons.Default.School
        "build" -> Icons.Default.Build
        "account_balance" -> Icons.Default.AccountBalance
        "work" -> Icons.Default.Work
        "category" -> Icons.Default.Category
        else -> Icons.Default.Category
    }
}

