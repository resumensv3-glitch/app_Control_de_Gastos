package com.example.ahorroplus.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.ahorroplus.ui.screens.AddTransactionScreen
import com.example.ahorroplus.ui.screens.CategoriesScreen
import com.example.ahorroplus.ui.screens.EditTransactionScreen
import com.example.ahorroplus.ui.screens.HomeScreen
import com.example.ahorroplus.ui.screens.WelcomeScreen
import com.example.ahorroplus.ui.viewmodel.CategoriaViewModel
import com.example.ahorroplus.ui.viewmodel.HomeViewModel
import com.example.ahorroplus.ui.viewmodel.TransaccionViewModel

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object Home : Screen("home")
    object AddTransaction : Screen("add_transaction")
    object EditTransaction : Screen("edit_transaction/{transactionId}") {
        fun createRoute(transactionId: Long) = "edit_transaction/$transactionId"
    }
    object Categories : Screen("categories")
}

@Composable
fun NavGraph(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    transaccionViewModel: TransaccionViewModel,
    categoriaViewModel: CategoriaViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Welcome.route
    ) {
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        // Limpiar el back stack para que no se pueda volver a welcome
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = homeViewModel,
                onAddTransactionClick = {
                    navController.navigate(Screen.AddTransaction.route)
                },
                onCategoriesClick = {
                    navController.navigate(Screen.Categories.route)
                },
                onEditTransactionClick = { transaccion ->
                    navController.navigate(Screen.EditTransaction.createRoute(transaccion.id))
                }
            )
        }
        
        composable(Screen.AddTransaction.route) {
            // Resetear formulario al entrar a la pantalla
            androidx.compose.runtime.DisposableEffect(Unit) {
                transaccionViewModel.resetearFormulario()
                onDispose { }
            }
            AddTransactionScreen(
                viewModel = transaccionViewModel,
                onBack = {
                    transaccionViewModel.resetearFormulario()
                    navController.popBackStack()
                }
            )
        }
        
        composable(
            route = Screen.EditTransaction.route,
            arguments = listOf(navArgument("transactionId") { type = NavType.LongType })
        ) { backStackEntry ->
            val transactionId = backStackEntry.arguments?.getLong("transactionId") ?: 0L
            val transacciones by homeViewModel.transacciones.collectAsState()
            val transaccion = transacciones.firstOrNull { it.id == transactionId }
            
            if (transaccion != null) {
                EditTransactionScreen(
                    transaccion = transaccion,
                    viewModel = transaccionViewModel,
                    onBack = {
                        transaccionViewModel.resetearFormulario()
                        navController.popBackStack()
                    }
                )
            }
        }
        
        composable(Screen.Categories.route) {
            CategoriesScreen(
                viewModel = categoriaViewModel,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}



