/* Aplicacion Collita v1
 Participantes: Godos García Jesús Emmanuel 217o02950,
                Ortiz Sánchez Néstor Éibar 217o03062,
                Axel David Ruiz Vargas 217o03139,
                Ramiro Aguilar Morales 207o02190*/
package com.programobil.collita_frontenv_v3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.programobil.collita_frontenv_v3.ui.screens.*
import com.programobil.collita_frontenv_v3.ui.theme.CollitaFrontenvv3Theme
import com.programobil.collita_frontenv_v3.ui.viewmodel.LoginViewModel
import com.programobil.collita_frontenv_v3.ui.viewmodel.RegisterViewModel
import com.programobil.collita_frontenv_v3.ui.viewmodel.UserViewModel
import com.programobil.collita_frontenv_v3.ui.screens.AdminNavHost
import com.programobil.collita_frontenv_v3.ui.theme.AdminTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CollitaFrontenvv3Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val loginViewModel: LoginViewModel = viewModel()
                    val registerViewModel: RegisterViewModel = viewModel()
                    val userViewModel: UserViewModel = viewModel()

                    NavHost(
                        navController = navController,
                        startDestination = "login"
                    ) {
                        composable(
                            route = "login",
                            enterTransition = { fadeIn() },
                            exitTransition = { fadeOut() }
                        ) {
                            LoginScreen(
                                navController = navController,
                                viewModel = loginViewModel,
                                userViewModel = userViewModel
                            )
                        }
                        composable(
                            route = "register",
                            enterTransition = { fadeIn() },
                            exitTransition = { fadeOut() }
                        ) {
                            RegisterScreen(
                                navController = navController,
                                viewModel = registerViewModel
                            )
                        }
                        composable(
                            route = "dashboard",
                            enterTransition = { fadeIn() },
                            exitTransition = { fadeOut() }
                        ) {
                            DashboardScreen(
                                navController = navController,
                                viewModel = userViewModel
                            )
                        }
                        composable(
                            route = "admin_home",
                            enterTransition = { fadeIn() },
                            exitTransition = { fadeOut() }
                        ) {
                            AdminTheme {
                                AdminHomePageScreen(navController = navController)
                            }
                        }
                        composable(
                            route = "admin_nav",
                            enterTransition = { fadeIn() },
                            exitTransition = { fadeOut() }
                        ) {
                            AdminTheme {
                                AdminNavHost()
                            }
                        }
                    }
                }
            }
        }
    }
}