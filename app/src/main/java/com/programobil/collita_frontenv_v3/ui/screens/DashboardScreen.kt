package com.programobil.collita_frontenv_v3.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.programobil.collita_frontenv_v3.data.api.RetrofitClient
import com.programobil.collita_frontenv_v3.data.api.UserResponse
import com.programobil.collita_frontenv_v3.data.api.CanaDto
import com.programobil.collita_frontenv_v3.ui.viewmodel.UserViewModel
import com.programobil.collita_frontenv_v3.ui.viewmodel.CanaViewModel
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: UserViewModel = viewModel(),
    canaViewModel: CanaViewModel = viewModel()
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
                        0 -> HomeContent(userViewModel = viewModel)
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
fun HomeContent(
    userViewModel: UserViewModel = viewModel()
) {
    var showCosechaDialog by remember { mutableStateOf(false) }
    var showResumenDialog by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var descripcion by remember { mutableStateOf("") }
    var cantidadAranazos by remember { mutableStateOf("") }
    var tiempoInicio by remember { mutableStateOf<LocalDateTime?>(null) }
    var tiempoTranscurrido by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    // Actualizar tiempo transcurrido cada segundo
    LaunchedEffect(tiempoInicio) {
        while (tiempoInicio != null) {
            val ahora = LocalDateTime.now()
            val duracion = Duration.between(tiempoInicio, ahora)
            tiempoTranscurrido = String.format(
                "%02d:%02d:%02d",
                duracion.toHours(),
                duracion.toMinutesPart(),
                duracion.toSecondsPart()
            )
            kotlinx.coroutines.delay(1000)
        }
    }

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
                    text = "Estado Actual",
                    style = MaterialTheme.typography.titleLarge
                )
                Text("¿Tienes una sesión activa?")
                Text("¿Has registrado alguna acción hoy?")
            }
        }

        if (tiempoInicio == null) {
            Button(
                onClick = { showCosechaDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Iniciar Cosecha")
            }
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Tiempo transcurrido: $tiempoTranscurrido",
                    style = MaterialTheme.typography.titleMedium
                )
                Button(
                    onClick = { showResumenDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Detener Cosecha")
                }
            }
        }

        if (showCosechaDialog) {
            AlertDialog(
                onDismissRequest = { showCosechaDialog = false },
                title = { Text("Nueva Cosecha") },
                text = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = { imagePicker.launch("image/*") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(if (selectedImageUri == null) "Seleccionar Imagen" else "Cambiar Imagen")
                        }

                        if (selectedImageUri != null) {
                            Text("Imagen seleccionada")
                        }

                        OutlinedTextField(
                            value = descripcion,
                            onValueChange = { descripcion = it },
                            label = { Text("Descripción") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            tiempoInicio = LocalDateTime.now()
                            showCosechaDialog = false
                        },
                        enabled = selectedImageUri != null && descripcion.isNotBlank(),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Iniciar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showCosechaDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }

        if (showResumenDialog) {
            AlertDialog(
                onDismissRequest = { showResumenDialog = false },
                title = { Text("Resumen de Cosecha") },
                text = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text("Tiempo total: $tiempoTranscurrido")
                        OutlinedTextField(
                            value = cantidadAranazos,
                            onValueChange = { 
                                if (it.isEmpty() || it.toIntOrNull() != null) {
                                    cantidadAranazos = it
                                }
                            },
                            label = { Text("Cantidad de Arañazos") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            scope.launch {
                                try {
                                    val now = LocalDateTime.now()
                                    val canaDto = CanaDto(
                                        idUsuario = userViewModel.getCurrentUserId() ?: "",
                                        horaInicioUsuario = tiempoInicio?.format(DateTimeFormatter.ofPattern("HH:mm:ss")) ?: now.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                                        horaFinalUsuario = now.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                                        cantidadCanaUsuario = cantidadAranazos.toDouble(),
                                        fecha = now.format(DateTimeFormatter.ISO_DATE),
                                        fechaUsuario = now.format(DateTimeFormatter.ISO_DATE),
                                        resumenCosecha = descripcion
                                    )
                                    
                                    RetrofitClient.canaService.createCana(canaDto)
                                    
                                    showResumenDialog = false
                                    tiempoInicio = null
                                    selectedImageUri = null
                                    descripcion = ""
                                    cantidadAranazos = ""
                                } catch (e: Exception) {
                                    showError = true
                                    errorMessage = "Error al guardar la cosecha: ${e.message}"
                                }
                            }
                        },
                        enabled = cantidadAranazos.isNotBlank(),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Terminar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showResumenDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }

        if (showError) {
            AlertDialog(
                onDismissRequest = { showError = false },
                title = { Text("Error") },
                text = { Text(errorMessage) },
                confirmButton = {
                    TextButton(onClick = { showError = false }) {
                        Text("Aceptar")
                    }
                }
            )
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
                    text = "Resumen del Día",
                    style = MaterialTheme.typography.titleLarge
                )
                Text("Total de trabajo realizado: 0")
                Text("Número de acciones registradas: 0")
                Text("Tiempo trabajado: 0 horas")
            }
        }
    }
}

data class CanaDto(
    val idUsuario: String,
    val fecha: String,
    val horaInicio: String,
    val horaFin: String,
    val cantidadAranazos: Double,
    val descripcion: String,
    val imagenUrl: String
) 