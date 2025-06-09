/* Aplicacion Collita v1
 Participantes: Godos García Jesús Emmanuel 217o02950,
                Ortiz Sánchez Néstor Éibar 217o03062,
                Peña Perez Axel  217o00677,
                Axel David Ruiz Vargas 217o03139,
                Ramiro Morales 207o02190*/

/**
 * DashboardScreen - Pantalla principal del usuario
 * 
 * Flujo de la pantalla:
 * 1. Al cargar, se obtienen los datos del usuario y sus pagos pendientes
 * 2. Se muestra un resumen de la actividad del día
 * 3. Se muestran los pagos pendientes en orden de fecha
 * 4. Se muestra el historial de pagos realizados
 * 
 * Componentes principales:
 * - ResumenDiario: Muestra estadísticas del día (cañas registradas, monto total)
 * - PagosPendientes: Lista de pagos por realizar con fecha y monto
 * - HistorialPagos: Lista de pagos realizados con fecha y estado
 * 
 * Interacciones:
 * - Al hacer clic en un pago pendiente, se muestra el detalle
 * - Al hacer clic en el botón de nueva caña, se navega a la pantalla de registro
 * - Al hacer clic en el botón de historial, se navega a la pantalla de historial
 */

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
import com.programobil.collita_frontenv_v3.network.RetrofitClient
import com.programobil.collita_frontenv_v3.data.api.UserResponse
import com.programobil.collita_frontenv_v3.data.api.CanaDto
import com.programobil.collita_frontenv_v3.data.api.PagoDto
import com.programobil.collita_frontenv_v3.ui.viewmodel.UserViewModel
import com.programobil.collita_frontenv_v3.ui.viewmodel.CanaViewModel
import com.programobil.collita_frontenv_v3.ui.viewmodel.HistorialCanaViewModel
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.launch
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.clickable
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: UserViewModel = viewModel(),
    canaViewModel: CanaViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    var selectedTab by remember { mutableStateOf(0) }
    val historialCanaViewModel = remember { HistorialCanaViewModel() }
    val idUsuario = viewModel.getCurrentUserId() ?: ""

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
                    icon = { Icon(Icons.Filled.Person, contentDescription = "Datos") },
                    label = { Text("Datos") },
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
                        1 -> HistorialCanaScreen(viewModel = historialCanaViewModel, idUsuario = idUsuario)
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

fun createPartFromString(value: String): RequestBody =
    value.toRequestBody("text/plain".toMediaTypeOrNull())

fun uriToFile(context: android.content.Context, uri: Uri): File {
    val inputStream = context.contentResolver.openInputStream(uri)
    val file = File(context.cacheDir, "upload_${System.currentTimeMillis()}.jpg")
    inputStream?.use { input ->
        file.outputStream().use { output ->
            input.copyTo(output)
        }
    }
    return file
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
    var isLoading by remember { mutableStateOf(false) }
    var canaId by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val idUsuario = userViewModel.getCurrentUserId() ?: ""
    var pagos by remember { mutableStateOf<List<PagoDto>>(emptyList()) }
    var canas by remember { mutableStateOf<Map<String, CanaDto>>(emptyMap()) }
    var isPagosLoading by remember { mutableStateOf(false) }

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

    // Cargar pagos y cañas asociadas al iniciar
    LaunchedEffect(idUsuario) {
        isPagosLoading = true
        try {
            val pagosUsuario = RetrofitClient.pagoService.getPagosByUsuario(idUsuario)
            pagos = pagosUsuario
            // Obtener ids únicos de cañas asociadas
            val canaIds = pagosUsuario.flatMap { it.detalleCanaIds ?: emptyList() }.distinct()
            val canasMap = mutableMapOf<String, CanaDto>()
            for (canaId in canaIds) {
                try {
                    val cana = RetrofitClient.canaService.getCanaById(canaId)
                    canasMap[canaId] = cana
                } catch (_: Exception) {}
            }
            canas = canasMap
        } catch (_: Exception) {
            pagos = emptyList()
            canas = emptyMap()
        } finally {
            isPagosLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Sección de tickets/pagos
        Text("Tus pagos", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        if (isPagosLoading) {
            CircularProgressIndicator()
        } else {
            val pagosPendientes = pagos.filter { it.estado == "pendiente" }
            val pagosCobrados = pagos.filter { it.estado == "pagado" }
            if (pagosPendientes.isEmpty() && pagosCobrados.isEmpty()) {
                Text("No tienes pagos registrados aún.")
            } else {
                if (pagosPendientes.isNotEmpty()) {
                    Text("Pendientes", fontWeight = FontWeight.SemiBold)
                    pagosPendientes.forEach { pago ->
                        PagoCardUsuario(pago, canas)
                    }
                }
                if (pagosCobrados.isNotEmpty()) {
                    Spacer(Modifier.height(8.dp))
                    Text("Historial de pagos", fontWeight = FontWeight.SemiBold)
                    pagosCobrados.forEach { pago ->
                        PagoCardUsuario(pago, canas)
                    }
                }
            }
        }
    }
}

@Composable
fun PagoCardUsuario(pago: PagoDto, canas: Map<String, CanaDto>) {
    val descripcion = pago.detalleCanaIds?.firstOrNull()?.let { canas[it]?.resumenCosecha } ?: "Sin descripción"
    val fechaCobro = pago.fechaPago ?: "-"
    val monto = pago.monto
    val estado = pago.estado
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("Descripción: $descripcion", fontWeight = FontWeight.Bold)
            Text("Monto: $${monto} MXN")
            Text("Fecha de cobro: $fechaCobro")
            Text("Estado: ${estado.capitalize()}")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    val navController = rememberNavController()
    val viewModel = viewModel<UserViewModel>()
    val canaViewModel = viewModel<CanaViewModel>()
    
    MaterialTheme {
        DashboardScreen(
            navController = navController,
            viewModel = viewModel,
            canaViewModel = canaViewModel
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeContentPreview() {
    MaterialTheme {
        HomeContent()
    }
}

@Preview(showBackground = true)
@Composable
fun ConfiguracionContentPreview() {
    val user = UserResponse(
        nombreUsuario = "Juan",
        apellidoPaternoUsuario = "Pérez",
        apellidoMaternoUsuario = "García",
        correo = "juan@example.com",
        telefono = "1234567890",
        curpUsuario = "PEGJ123456HDFABC01"
    )
    
    MaterialTheme {
        ConfiguracionContent(user)
    }
}

@Preview(showBackground = true)
@Composable
fun ExpandableCanaCardPreview() {
    val cana = CanaDto(
        idUsuario = "123",
        horaInicioUsuario = "08:00:00",
        horaFinalUsuario = "12:00:00",
        cantidadCanaUsuario = 150.0,
        fecha = "2024-03-20",
        fechaUsuario = "2024-03-20",
        resumenCosecha = "Cosecha exitosa"
    )
    
    MaterialTheme {
        ExpandableCanaCard(cana = cana)
    }
} 