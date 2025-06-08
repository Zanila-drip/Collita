package com.programobil.collita_frontenv_v3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.programobil.collita_frontenv_v3.data.api.RetrofitClient
import com.programobil.collita_frontenv_v3.ui.screens.*
import com.programobil.collita_frontenv_v3.ui.theme.CollitaFrontenvv3Theme
import com.programobil.collita_frontenv_v3.ui.viewmodel.LoginViewModel
import com.programobil.collita_frontenv_v3.ui.viewmodel.LoginViewModelFactory
import com.programobil.collita_frontenv_v3.ui.viewmodel.RegisterViewModel
import com.programobil.collita_frontenv_v3.ui.viewmodel.RegisterViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CollitaFrontenvv3Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val loginViewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(RetrofitClient.apiService))
    val registerViewModel: RegisterViewModel = viewModel(factory = RegisterViewModelFactory(RetrofitClient.apiService))

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onLoginSuccess = { userName ->
                    navController.navigate("dashboard/$userName") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate("register")
                },
                viewModel = loginViewModel
            )
        }
        composable("register") {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.popBackStack()
                },
                viewModel = registerViewModel
            )
        }
        composable(
            route = "dashboard/{userName}",
            arguments = listOf(navArgument("userName") { type = NavType.StringType })
        ) { backStackEntry ->
            val userName = backStackEntry.arguments?.getString("userName") ?: ""
            DashboardScreen(
                userName = userName,
                navController = navController
            )
        }
    }
}