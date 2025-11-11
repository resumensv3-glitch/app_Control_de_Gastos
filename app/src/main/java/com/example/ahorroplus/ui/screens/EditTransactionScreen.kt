package com.example.ahorroplus.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
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
import com.example.ahorroplus.data.model.Transaccion
import com.example.ahorroplus.ui.viewmodel.TransaccionViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTransactionScreen(
    transaccion: Transaccion,
    viewModel: TransaccionViewModel,
    onBack: () -> Unit
) {
    val monto by viewModel.monto.collectAsState()
    val descripcion by viewModel.descripcion.collectAsState()
    val tipoSeleccionado by viewModel.tipoSeleccionado.collectAsState()
    val categoriaSeleccionada by viewModel.categoriaSeleccionada.collectAsState()
    val categorias by viewModel.categorias.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()

    // Cargar datos de la transacción al entrar
    LaunchedEffect(transaccion.id) {
        viewModel.setMonto(transaccion.monto.toString())
        viewModel.setDescripcion(transaccion.descripcion)
        viewModel.setTipo(transaccion.tipo)
    }
    
    // Cargar categoría cuando esté disponible
    LaunchedEffect(categorias, transaccion.categoriaId) {
        categorias.firstOrNull { it.id == transaccion.categoriaId }?.let {
            viewModel.setCategoria(it)
        }
    }

    // Efecto para navegar de vuelta después de actualizar exitosamente
    LaunchedEffect(key1 = mensaje) {
        if (mensaje?.contains("exitosamente") == true) {
            kotlinx.coroutines.delay(1000)
            onBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Transacción", fontWeight = FontWeight.Bold) },
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
            // Selección de Tipo
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

            // Campo de Monto
            OutlinedTextField(
                value = monto,
                onValueChange = { viewModel.setMonto(it) },
                label = { Text("Monto") },
                leadingIcon = { Icon(Icons.Default.AttachMoney, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            // Campo de Descripción
            OutlinedTextField(
                value = descripcion,
                onValueChange = { viewModel.setDescripcion(it) },
                label = { Text("Descripción") },
                leadingIcon = { Icon(Icons.Default.Description, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Selección de Categoría
            Text(
                text = "Categoría (Opcional)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            if (categorias.isEmpty()) {
                Text(
                    text = "No hay categorías disponibles.",
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

            // Botón Actualizar
            Button(
                onClick = { viewModel.actualizarTransaccion(transaccion) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = tipoSeleccionado != null
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Actualizar Transacción", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

