package com.example.ahorroplus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.ahorroplus.navigation.NavGraph
import com.example.ahorroplus.ui.theme.AhorroPlusTheme
import com.example.ahorroplus.ui.viewmodel.CategoriaViewModel
import com.example.ahorroplus.ui.viewmodel.HomeViewModel
import com.example.ahorroplus.ui.viewmodel.TransaccionViewModel
import com.example.ahorroplus.util.DatabaseInitializer

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val application = applicationContext as AhorroPlusApplication
        val repository = application.repository
        
        // Inicializar categorías por defecto
        DatabaseInitializer.initializeDatabase(repository)
        
        setContent {
            AhorroPlusTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    
                    val homeViewModel: HomeViewModel = viewModel(
                        factory = HomeViewModel.provideFactory(repository)
                    )
                    val transaccionViewModel: TransaccionViewModel = viewModel(
                        factory = TransaccionViewModel.provideFactory(repository)
                    )
                    val categoriaViewModel: CategoriaViewModel = viewModel(
                        factory = CategoriaViewModel.provideFactory(repository)
                    )
                    
                    NavGraph(
                        navController = navController,
                        homeViewModel = homeViewModel,
                        transaccionViewModel = transaccionViewModel,
                        categoriaViewModel = categoriaViewModel
                    )
                }
            }
        }
    }
}