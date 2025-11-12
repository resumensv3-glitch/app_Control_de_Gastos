package com.example.ahorroplus.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ahorroplus.data.model.Categoria
import com.example.ahorroplus.data.model.TipoTransaccion
import com.example.ahorroplus.data.model.Transaccion
import com.example.ahorroplus.ui.viewmodel.HomeViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onAddTransactionClick: () -> Unit,
    onCategoriesClick: () -> Unit,
    onEditTransactionClick: (Transaccion) -> Unit
) {
    val saldo by viewModel.saldo.collectAsState(initial = 0.0)
    val totalIngresos by viewModel.totalIngresos.collectAsState(initial = 0.0)
    val totalEgresos by viewModel.totalEgresos.collectAsState(initial = 0.0)
    val transacciones by viewModel.transacciones.collectAsState(initial = emptyList())
    val tipoFiltro by viewModel.tipoFiltro.collectAsState(initial = null)
    val categoriaFiltro by viewModel.categoriaFiltro.collectAsState(initial = null)
    val fechaInicio by viewModel.fechaInicio.collectAsState(initial = null)
    val fechaFin by viewModel.fechaFin.collectAsState(initial = null)
    val categorias by viewModel.categorias.collectAsState(initial = emptyList())

    var showFechaInicioPicker by rememberSaveable { mutableStateOf(false) }
    var showFechaFinPicker by rememberSaveable { mutableStateOf(false) }

    val fechaInicioPickerState = rememberDatePickerState(initialSelectedDateMillis = fechaInicio)
    val fechaFinPickerState = rememberDatePickerState(initialSelectedDateMillis = fechaFin)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AhorroPlus", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onCategoriesClick) {
                        Icon(Icons.Default.Category, contentDescription = "Categorías")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddTransactionClick,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar transacción")
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Tarjeta de saldo
                item {
                    BalanceCard(saldo)
                }

                // Tarjetas de resumen
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SummaryCard(
                            title = "Ingresos",
                            amount = totalIngresos,
                            icon = Icons.AutoMirrored.Filled.TrendingUp,
                            color = Color(0xFF4CAF50),
                            modifier = Modifier.weight(1f)
                        )
                        SummaryCard(
                            title = "Egresos",
                            amount = totalEgresos,
                            icon = Icons.AutoMirrored.Filled.TrendingDown,
                            color = Color(0xFFF44336),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Filtros
                item {
                    FilterSection(
                        tipoFiltro = tipoFiltro,
                        categoriaFiltro = categoriaFiltro,
                        fechaInicio = fechaInicio,
                        fechaFin = fechaFin,
                        categorias = categorias,
                        onTipoFilterChange = { viewModel.setTipoFiltro(it) },
                        onCategoriaFilterChange = { viewModel.setCategoriaFiltro(it) },
                        onFechaInicioClick = { showFechaInicioPicker = true },
                        onFechaFinClick = { showFechaFinPicker = true },
                        onClearFilters = { viewModel.limpiarFiltros() }
                    )
                }

                // Gráfica de gastos
                item {
                    if (transacciones.isNotEmpty()) {
                        ExpenseChartCard(transacciones)
                    }
                }

                // Lista de transacciones
                item {
                    Text(
                        "Transacciones",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                if (transacciones.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Default.Receipt,
                                    contentDescription = null,
                                    modifier = Modifier.size(64.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text("No hay transacciones")
                            }
                        }
                    }
                } else {
                    items(transacciones) { transaccion ->
                        TransactionItem(
                            transaccion = transaccion,
                            onDelete = { viewModel.deleteTransaccion(transaccion) },
                            onEdit = { onEditTransactionClick(transaccion) }
                        )
                    }
                }
            }

            // Selectores de fecha
            if (showFechaInicioPicker) {
                DatePickerDialog(
                    onDismissRequest = { showFechaInicioPicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            fechaInicioPickerState.selectedDateMillis?.let {
                                viewModel.setFechaInicio(it)
                            }
                            showFechaInicioPicker = false
                        }) { Text("OK") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showFechaInicioPicker = false }) { Text("Cancelar") }
                    }
                ) {
                    DatePicker(state = fechaInicioPickerState)
                }
            }

            if (showFechaFinPicker) {
                DatePickerDialog(
                    onDismissRequest = { showFechaFinPicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            fechaFinPickerState.selectedDateMillis?.let {
                                viewModel.setFechaFin(it)
                            }
                            showFechaFinPicker = false
                        }) { Text("OK") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showFechaFinPicker = false }) { Text("Cancelar") }
                    }
                ) {
                    DatePicker(state = fechaFinPickerState)
                }
            }
        }
    }
}

// Tarjeta de saldo
@Composable
fun BalanceCard(saldo: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(24.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Saldo Total", style = MaterialTheme.typography.titleMedium)
            Text(
                "$${String.format("%.2f", saldo)}",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = if (saldo >= 0) Color(0xFF4CAF50) else Color(0xFFF44336)
            )
        }
    }
}

// Tarjeta resumen
@Composable
fun SummaryCard(title: String, amount: Double, icon: ImageVector, color: Color, modifier: Modifier = Modifier) {
    Card(modifier = modifier, shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, null, tint = color, modifier = Modifier.size(32.dp))
            Text(title, style = MaterialTheme.typography.bodyMedium)
            Text(
                "$${String.format("%.2f", amount)}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

// Item de transacción
@Composable
fun TransactionItem(transaccion: Transaccion, onDelete: () -> Unit, onEdit: () -> Unit) {
    val esIngreso = transaccion.tipo == TipoTransaccion.INGRESO
    val color = if (esIngreso) Color(0xFF4CAF50) else Color(0xFFF44336)
    val icon = if (esIngreso) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward
    val fecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(transaccion.fecha))

    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = color, modifier = Modifier.size(32.dp))
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(transaccion.descripcion, fontWeight = FontWeight.Medium)
                Text(fecha, style = MaterialTheme.typography.bodySmall)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${if (esIngreso) "+" else "-"}$${String.format("%.2f", transaccion.monto)}",
                    fontWeight = FontWeight.Bold,
                    color = color
                )
                Row {
                    IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, null) }
                    IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, null) }
                }
            }
        }
    }
}

// Filtros
@Composable
fun FilterSection(
    tipoFiltro: TipoTransaccion?,
    categoriaFiltro: Long?,
    fechaInicio: Long?,
    fechaFin: Long?,
    categorias: List<Categoria>,
    onTipoFilterChange: (TipoTransaccion?) -> Unit,
    onCategoriaFilterChange: (Long?) -> Unit,
    onFechaInicioClick: () -> Unit,
    onFechaFinClick: () -> Unit,
    onClearFilters: () -> Unit
) {
    val df = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Filtros", fontWeight = FontWeight.Bold)
                TextButton(onClick = onClearFilters) {
                    Icon(Icons.Default.Clear, null, modifier = Modifier.size(18.dp))
                    Text("Limpiar")
                }
            }

            // Tipo
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = tipoFiltro == null,
                    onClick = { onTipoFilterChange(null) },
                    label = { Text("Todos") }
                )
                FilterChip(
                    selected = tipoFiltro == TipoTransaccion.INGRESO,
                    onClick = {
                        onTipoFilterChange(if (tipoFiltro == TipoTransaccion.INGRESO) null else TipoTransaccion.INGRESO)
                    },
                    label = { Text("Ingresos") },
                    leadingIcon = { Icon(Icons.Default.ArrowUpward, null, Modifier.size(18.dp)) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF4CAF50).copy(alpha = 0.2f),
                        selectedLabelColor = Color(0xFF4CAF50)
                    )
                )
                FilterChip(
                    selected = tipoFiltro == TipoTransaccion.EGRESO,
                    onClick = {
                        onTipoFilterChange(if (tipoFiltro == TipoTransaccion.EGRESO) null else TipoTransaccion.EGRESO)
                    },
                    label = { Text("Egresos") },
                    leadingIcon = { Icon(Icons.Default.ArrowDownward, null, Modifier.size(18.dp)) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFF44336).copy(alpha = 0.2f),
                        selectedLabelColor = Color(0xFFF44336)
                    )
                )
            }

            // Categorías
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    FilterChip(
                        selected = categoriaFiltro == null,
                        onClick = { onCategoriaFilterChange(null) },
                        label = { Text("Todas") }
                    )
                }
                items(categorias) { cat ->
                    val color = try {
                        Color(android.graphics.Color.parseColor(cat.color))
                    } catch (e: Exception) {
                        MaterialTheme.colorScheme.primary
                    }
                    FilterChip(
                        selected = categoriaFiltro == cat.id,
                        onClick = {
                            onCategoriaFilterChange(if (categoriaFiltro == cat.id) null else cat.id)
                        },
                        label = { Text(cat.nombre) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = color.copy(alpha = 0.2f),
                            selectedLabelColor = color
                        )
                    )
                }
            }

            // Fechas
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onFechaInicioClick, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.CalendarToday, null, Modifier.size(18.dp))
                    Text(fechaInicio?.let { df.format(Date(it)) } ?: "Desde")
                }
                OutlinedButton(onClick = onFechaFinClick, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.CalendarToday, null, Modifier.size(18.dp))
                    Text(fechaFin?.let { df.format(Date(it)) } ?: "Hasta")
                }
            }
        }
    }
}

// Gráfica de egresos por categoría
@Composable
fun ExpenseChartCard(transacciones: List<Transaccion>) {
    val egresos = transacciones.filter { it.tipo == TipoTransaccion.EGRESO }
    val total = egresos.sumOf { it.monto }

    if (total == 0.0) return

    val categorias = egresos.groupBy { it.categoriaId }
        .mapValues { (_, list) -> list.sumOf { it.monto } }
        .toList()
        .sortedByDescending { it.second }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Gastos por Categoría", fontWeight = FontWeight.Bold)
            categorias.forEach { (id, monto) ->
                val porcentaje = (monto / total * 100).roundToInt()
                Row(
                    Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Categoría #$id")
                    Text("$${String.format("%.2f", monto)} (${porcentaje}%)")
                }
                LinearProgressIndicator(
                    progress = (monto / total).toFloat(),
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFF44336),
                    trackColor = Color.LightGray.copy(alpha = 0.3f)
                )
            }
        }
    }
}
