package com.example.ahorroplus.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ahorroplus.data.model.Categoria
import com.example.ahorroplus.ui.viewmodel.CategoriaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
    viewModel: CategoriaViewModel,
    onBack: () -> Unit
) {
    val categorias by viewModel.categorias.collectAsState()
    val nombre by viewModel.nombre.collectAsState()
    val iconoSeleccionado by viewModel.iconoSeleccionado.collectAsState()
    val colorSeleccionado by viewModel.colorSeleccionado.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Categorías", fontWeight = FontWeight.Bold) },
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Nueva Categoría",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        OutlinedTextField(
                            value = nombre,
                            onValueChange = { viewModel.setNombre(it) },
                            label = { Text("Nombre") },
                            leadingIcon = { Icon(Icons.Default.Label, contentDescription = null) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Text(
                            text = "Selecciona un Icono",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium
                        )

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(5),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .height(200.dp)
                                .fillMaxWidth()
                        ) {
                            items(iconosDisponibles) { icono ->
                                IconButton(
                                    onClick = { viewModel.setIcono(icono.first) },
                                    modifier = Modifier
                                        .aspectRatio(1f)
                                        .background(
                                            if (iconoSeleccionado == icono.first) {
                                                MaterialTheme.colorScheme.primaryContainer
                                            } else {
                                                Color.Transparent
                                            },
                                            RoundedCornerShape(8.dp)
                                        )
                                ) {
                                    Icon(
                                        icono.second,
                                        contentDescription = icono.first,
                                        tint = if (iconoSeleccionado == icono.first) {
                                            MaterialTheme.colorScheme.onPrimaryContainer
                                        } else {
                                            MaterialTheme.colorScheme.onSurface
                                        }
                                    )
                                }
                            }
                        }

                        Text(
                            text = "Selecciona un Color",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium
                        )

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(6),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .height(140.dp)
                                .fillMaxWidth()
                        ) {
                            items(coloresDisponibles) { colorPair ->
                                val (nombreColor, color) = colorPair
                                Box(
                                    modifier = Modifier
                                        .aspectRatio(1f)
                                        .background(color, RoundedCornerShape(8.dp))
                                        .then(
                                            if (colorSeleccionado == nombreColor) {
                                                Modifier.border(
                                                    3.dp,
                                                    MaterialTheme.colorScheme.primary,
                                                    RoundedCornerShape(8.dp)
                                                )
                                            } else Modifier
                                        )
                                        .clickable { viewModel.setColor(nombreColor) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (colorSeleccionado == nombreColor) {
                                        Icon(
                                            Icons.Default.Check,
                                            contentDescription = null,
                                            tint = Color.White
                                        )
                                    }
                                }
                            }
                        }

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

                        Button(
                            onClick = { viewModel.guardarCategoria() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Agregar Categoría")
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Categorías Existentes",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            if (categorias.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.Category,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "No hay categorías",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(categorias) { categoria ->
                    CategoryItem(
                        categoria = categoria,
                        onDelete = { viewModel.deleteCategoria(categoria) }
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryItem(
    categoria: Categoria,
    onDelete: () -> Unit
) {
    val color = try {
        Color(android.graphics.Color.parseColor(categoria.color))
    } catch (e: Exception) {
        MaterialTheme.colorScheme.primary
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    getIconByName(categoria.icono),
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(32.dp)
                )
                Text(
                    text = categoria.nombre,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

val iconosDisponibles = listOf(
    "restaurant" to Icons.Default.Restaurant,
    "directions_car" to Icons.Default.DirectionsCar,
    "movie" to Icons.Default.Movie,
    "local_hospital" to Icons.Default.LocalHospital,
    "shopping_cart" to Icons.Default.ShoppingCart,
    "school" to Icons.Default.School,
    "build" to Icons.Default.Build,
    "account_balance" to Icons.Default.AccountBalance,
    "work" to Icons.Default.Work,
    "category" to Icons.Default.Category,
    "home" to Icons.Default.Home,
    "flight" to Icons.Default.Flight,
    "fitness_center" to Icons.Default.FitnessCenter,
    "sports_esports" to Icons.Default.SportsEsports,
    "pets" to Icons.Default.Pets
)

// Más colores añadidos
val coloresDisponibles = listOf(
    "#4CAF50" to Color(0xFF4CAF50),
    "#8BC34A" to Color(0xFF8BC34A),
    "#CDDC39" to Color(0xFFCDDC39),
    "#FFEB3B" to Color(0xFFFFEB3B),
    "#FFC107" to Color(0xFFFFC107),
    "#FF9800" to Color(0xFFFF9800),
    "#FF5722" to Color(0xFFFF5722),
    "#F44336" to Color(0xFFF44336),
    "#E91E63" to Color(0xFFE91E63),
    "#FFB6C1" to Color(0xFFFFB6C1),
    "#9C27B0" to Color(0xFF9C27B0),
    "#B39DDB" to Color(0xFFB39DDB),
    "#3F51B5" to Color(0xFF3F51B5),
    "#2196F3" to Color(0xFF2196F3),
    "#00BCD4" to Color(0xFF00BCD4),
    "#00E5FF" to Color(0xFF00E5FF),
    "#009688" to Color(0xFF009688),
    "#607D8B" to Color(0xFF607D8B),
    "#795548" to Color(0xFF795548),
    "#9E9E9E" to Color(0xFF9E9E9E),
    "#000000" to Color(0xFF000000),
    "#FFFFFF" to Color(0xFFFFFFFF),
    "#FFA726" to Color(0xFFFFA726),
    "#C0CA33" to Color(0xFFC0CA33),
    "#00C853" to Color(0xFF00C853)
)
