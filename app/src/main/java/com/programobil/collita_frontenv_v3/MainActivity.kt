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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.programobil.collita_frontenv_v3.ui.screens.HomePageScreen
import com.programobil.collita_frontenv_v3.ui.screens.LoginScreen
import com.programobil.collita_frontenv_v3.ui.theme.CollitaFrontenvv3Theme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.programobil.collita_frontenv_v3.data.api.ApiService
import com.programobil.collita_frontenv_v3.ui.viewmodel.LoginViewModel
import com.programobil.collita_frontenv_v3.ui.viewmodel.LoginState

class MainActivity : ComponentActivity() {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/") // URL para el emulador Android
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(ApiService::class.java)
    private val loginViewModel = LoginViewModel(apiService)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CollitaFrontenvv3Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val loginState by loginViewModel.loginState.collectAsState()

                    // Observar el estado de login para navegar
                    LaunchedEffect(loginState) {
                        if (loginState is LoginState.Success) {
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    }

                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") {
                            LoginScreen(viewModel = loginViewModel)
                        }
                        composable("home") {
                            HomePageScreen()
                        }
                    }
                }
            }
        }
    }
}