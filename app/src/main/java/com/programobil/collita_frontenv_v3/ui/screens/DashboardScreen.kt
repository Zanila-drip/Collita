package com.programobil.collita_frontenv_v3.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.programobil.collita_frontenv_v3.data.api.UserResponse
import com.programobil.collita_frontenv_v3.ui.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: UserViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    var selectedTab by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.loadUser()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    when (state) {
                        is UserViewModel.UserState.Success -> {
                            val user = (state as UserViewModel.UserState.Success).user
                            val primerNombre = user.nombreUsuario?.split(" ")?.first() ?: "Usuario"
                            Text("👋 ¡Hola, $primerNombre!")
                        }
                        else -> Text("Dashboard")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.logout()
                        navController.navigate("login") {
                            popUpTo("dashboard") { inclusive = true }
                            launchSingleTop = true
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Cerrar sesión")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.List, contentDescription = "Historial") },
                    label = { Text("Historial") },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Settings, contentDescription = "Configuración") },
                    label = { Text("Configuración") },
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 }
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (state) {
                is UserViewModel.UserState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is UserViewModel.UserState.Success -> {
                    val user = (state as UserViewModel.UserState.Success).user
                    when (selectedTab) {
                        0 -> HomeContent()
                        1 -> HistorialContent()
                        2 -> ConfiguracionContent(user)
                    }
                }
                is UserViewModel.UserState.Error -> {
                    Text(
                        text = "Error: ${(state as UserViewModel.UserState.Error).message}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                UserViewModel.UserState.Initial -> {
                    Text(
                        text = "Iniciando...",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                UserViewModel.UserState.LoggedOut -> {
                    Text(
                        text = "Sesión cerrada",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
private fun DatosContent(user: UserResponse) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "${user.nombreUsuario}",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "${user.apellidoPaternoUsuario} ${user.apellidoMaternoUsuario}",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = user.correo ?: "",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = user.telefono ?: "",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = user.curpUsuario ?: "",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun HistorialContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Historial de actividades")
    }
}

@Composable
private fun ConfiguracionContent(user: UserResponse) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "${user.nombreUsuario}",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "${user.apellidoPaternoUsuario} ${user.apellidoMaternoUsuario}",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = user.correo ?: "",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = user.telefono ?: "",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = user.curpUsuario ?: "",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun HomeContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Estado actual
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Estado actual",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "¿Tienes una sesión activa hoy?",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "¿Ya registraste alguna acción?",
                    style = MaterialTheme.typography.bodyLarge
                )
                Button(
                    onClick = { /* TODO */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Iniciar nueva acción")
                }
                Text(
                    text = "Última acción registrada:",
                    style = MaterialTheme.typography.bodyMedium
                )
                // TODO: Mostrar detalles de última acción
            }
        }

        // Resumen del día
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Resumen del día",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Total de caña trabajada hoy: 0 kg",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Número de acciones registradas: 0",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Tiempo trabajado: 0 horas",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
} 