/* Aplicacion Collita v1
 Participantes: Godos García Jesús Emmanuel 217o02950,
                Ortiz Sánchez Néstor Éibar 217o03062,
                Axel David Ruiz Vargas 217o03139,
                Ramiro Aguilar Morales 207o02190*/

/**
 * AdminHomePageScreen - Pantalla principal del administrador
 * 
 * Flujo de la pantalla:
 * 1. Se muestran las secciones principales:
 *    - Gestión de usuarios
 *    - Gestión de pagos
 *    - Reportes y estadísticas
 * 
 * Componentes principales:
 * - Dashboard: Muestra KPIs principales en cards
 * - MenúNavegación: Acceso rápido a las diferentes secciones
 * - ListaAcciones: Acciones comunes del administrador
 * 
 * Interacciones:
 * - Al hacer clic en una sección, se navega a la pantalla correspondiente
 * - Al hacer clic en un KPI, se muestra el detalle
 * - Al hacer clic en una acción, se ejecuta la operación correspondiente
 */

package com.programobil.collita_frontenv_v3.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.programobil.collita_frontenv_v3.ui.viewmodel.AdminViewModel
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Info
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import com.programobil.collita_frontenv_v3.data.api.UserResponse
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.clickable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.runtime.LaunchedEffect
import com.programobil.collita_frontenv_v3.data.api.CanaDto
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import android.app.DatePickerDialog
import androidx.compose.ui.platform.LocalContext
import java.util.Calendar
import com.programobil.collita_frontenv_v3.data.api.UsuarioDto
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import com.programobil.collita_frontenv_v3.network.RetrofitClient
import androidx.compose.ui.tooling.preview.Preview
import com.programobil.collita_frontenv_v3.ui.components.UsuarioCardHistorialStyle
import com.programobil.collita_frontenv_v3.ui.theme.AdminTheme
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import com.programobil.collita_frontenv_v3.ui.viewmodel.PagosViewModel
import com.programobil.collita_frontenv_v3.ui.viewmodel.PagosState
import com.programobil.collita_frontenv_v3.ui.screens.admin.PagosPendientesSection
import com.programobil.collita_frontenv_v3.ui.screens.admin.AdminUsuariosScreen
import com.programobil.collita_frontenv_v3.ui.screens.admin.AdminDatosScreen
import com.programobil.collita_frontenv_v3.ui.screens.admin.AdminReportesScreen
import com.programobil.collita_frontenv_v3.ui.screens.admin.CanasDeUsuarioScreen
import com.programobil.collita_frontenv_v3.ui.screens.admin.AdminPagosScreen

@Composable
fun AdminHomePageScreen(navController: NavController) {
    AdminTheme {
        AdminHomePageScreenContent(navController)
    }
}

@Composable
private fun AdminHomePageScreenContent(navController: NavController) {
    val navHostController = rememberNavController()
    val viewModel: AdminViewModel = viewModel()
    val usuarios = viewModel.usuarios
    val error = viewModel.error
    var searchQuery by remember { mutableStateOf("") }
    var usuarioSeleccionado by remember { mutableStateOf<UserResponse?>(null) }

    // Filtrado de usuarios (excluyendo admin Ramiro)
    val filteredUsuarios = remember(searchQuery, usuarios) {
        usuarios.filter { usuario ->
            val query = searchQuery.trim().lowercase()
            val esRamiro = usuario.correo?.trim()?.lowercase() == "ramiro07@email.com" ||
                usuario.curpUsuario?.trim()?.lowercase() == "curpadmin123"
            (query.isEmpty() ||
                usuario.nombreUsuario?.lowercase()?.contains(query) == true ||
                usuario.apellidoPaternoUsuario?.lowercase()?.contains(query) == true ||
                usuario.apellidoMaternoUsuario?.lowercase()?.contains(query) == true ||
                usuario.curpUsuario?.lowercase()?.contains(query) == true ||
                usuario.correo?.lowercase()?.contains(query) == true
            ) && !esRamiro
        }
    }

    Scaffold { paddingValues ->
        NavHost(
            navController = navHostController,
            startDestination = "dashboard",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("dashboard") {
                AdminDashboardResumen()
            }
            composable("pagos") { AdminPagosScreen() }
            composable("usuarios") {
                if (usuarioSeleccionado == null) {
                    AdminUsuariosScreen(
                        usuarios = filteredUsuarios,
                        error = error,
                        searchQuery = searchQuery,
                        onSearchQueryChange = { searchQuery = it },
                        onDeleteUsuario = { user -> viewModel.eliminarUsuario(user.id ?: "") },
                        onShowCanas = { usuario -> usuarioSeleccionado = usuario }
                    )
                } else {
                    CanasDeUsuarioScreen(
                        usuario = usuarioSeleccionado!!,
                        onClose = { usuarioSeleccionado = null }
                    )
                }
            }
            composable("datos") { AdminDatosScreen(navController = navController) }
            composable("reportes") { AdminReportesScreen() }
        }
    }
}

@Composable
private fun ErrorMessage(error: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Error",
                    tint = MaterialTheme.colorScheme.error
                )
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
private fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun AdminInicioScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Pantalla de Inicio Admin", style = MaterialTheme.typography.titleLarge)
    }
}

@Composable
fun AdminDatosScreen(navController: NavController? = null) {
    val viewModel: AdminViewModel = viewModel()
    val admin = viewModel.usuarios.firstOrNull { it.correo?.contains("ramiro", ignoreCase = true) == true }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "Datos del Administrador",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Divider()
        if (admin != null) {
            Text(text = "Nombre:", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
            Text(text = "${admin.nombreUsuario} ${admin.apellidoPaternoUsuario} ${admin.apellidoMaternoUsuario}", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Correo:", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
            Text(text = admin.correo ?: "-", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Teléfono:", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
            Text(text = admin.telefono ?: "-", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "CURP:", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
            Text(text = admin.curpUsuario ?: "-", style = MaterialTheme.typography.bodyLarge)
        } else {
            Text("No se encontraron datos del administrador.", color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun AdminReportesScreen() {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var showDatePicker by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val formattedDate = selectedDate.format(DateTimeFormatter.ISO_DATE)
    var canas by remember { mutableStateOf<List<CanaDto>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(formattedDate) {
        isLoading = true
        try {
            scope.launch {
                canas = RetrofitClient.canaService.getCanaByFecha(formattedDate)
                error = null
            }
        } catch (e: Exception) {
            error = "Error al cargar los datos: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "Reportes",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Divider()

        // Selector de fecha
        OutlinedTextField(
            value = formattedDate,
            onValueChange = { },
            label = { Text("Fecha") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(Icons.Default.DateRange, contentDescription = "Seleccionar fecha")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        if (showDatePicker) {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                    showDatePicker = false
                },
                selectedDate.year,
                selectedDate.monthValue - 1,
                selectedDate.dayOfMonth
            ).show()
        }

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else if (error != null) {
            Text(
                text = error!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
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
                        text = "Resumen del día $selectedDate",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Total de participantes: ${canas.map { it.idUsuario }.distinct().size}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Total de arañazos: ${canas.sumOf { it.cantidadCanaUsuario }}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Costo del día: $${canas.sumOf { it.cantidadCanaUsuario * 80 }.toInt()} MXN",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            // Lista de cañas del día
            if (canas.isNotEmpty()) {
                Text(
                    text = "Detalle de cañas",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp)
                )
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(canas) { cana ->
                        ExpandableCanaCard(cana)
                    }
                }
            } else {
                Text(
                    text = "No hay cañas registradas para esta fecha",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Composable
fun ExpandableCanaCard(cana: CanaDto) {
    var expanded by remember { mutableStateOf(false) }
    var usuario by remember { mutableStateOf<UsuarioDto?>(null) }
    var imageUrl by remember { mutableStateOf<String?>(null) }
    var isLoadingImage by remember { mutableStateOf(false) }
    var imageError by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    fun cargarImagen() {
        scope.launch {
            try {
                println("DEBUG: Cargando usuario para ID: ${cana.idUsuario}")
                usuario = RetrofitClient.usuarioService.getUsuarioById(cana.idUsuario)
                println("DEBUG: Usuario cargado: ${usuario?.nombreUsuario}")
                
                // Construir la URL de la imagen con la ruta correcta
                imageUrl = "http://64.23.166.183:8080/api/v1/cana/${cana.id}/imagen"
                println("DEBUG: URL de imagen construida: $imageUrl")
                println("DEBUG: ID de la caña: ${cana.id}")
                isLoadingImage = true
                imageError = null
            } catch (e: Exception) {
                println("ERROR: Error al cargar datos: ${e.message}")
                e.printStackTrace()
                imageError = "Error al cargar datos: ${e.message}"
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { expanded = !expanded },
        onClick = {
            expanded = !expanded
            if (expanded && usuario == null) {
                cargarImagen()
            }
        },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row {
                Text(
                    text = when {
                        usuario == null && !isLoadingImage && imageError != null -> "Usuario eliminado"
                        usuario?.nombreUsuario != null -> usuario?.nombreUsuario!!
                        else -> "Cargando..."
                    },
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Arañazos: ${cana.cantidadCanaUsuario}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            if (expanded) {
                Divider(Modifier.padding(vertical = 4.dp))
                Text("CURP: ${usuario?.curpUsuario ?: if (!isLoadingImage && imageError != null) "Usuario eliminado" else "Cargando..."}")
                Text("ID: ${cana.idUsuario}")
                Text("Hora inicio: ${cana.horaInicioUsuario}")
                Text("Hora fin: ${cana.horaFinalUsuario}")
                
                // Mostrar la imagen si existe
                if (isLoadingImage) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                
                imageError?.let { error ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Error",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No se pudo cargar la imagen",
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = { cargarImagen() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Reintentar",
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Reintentar")
                            }
                        }
                    }
                }
                
                imageUrl?.let { url ->
                    AsyncImage(
                        model = url,
                        contentDescription = "Imagen de la caña",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop,
                        onLoading = { 
                            println("DEBUG: Iniciando carga de imagen desde URL: $url")
                            isLoadingImage = true 
                        },
                        onSuccess = { 
                            println("DEBUG: Imagen cargada exitosamente")
                            isLoadingImage = false 
                            imageError = null
                        },
                        onError = { 
                            println("ERROR: Error al cargar la imagen: ${it.result.throwable.message}")
                            isLoadingImage = false
                            imageError = "Error al cargar la imagen"
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AdminUsuariosScreen() {
    val viewModel: AdminViewModel = viewModel()
    val usuarios = viewModel.usuarios
    val error = viewModel.error
    var searchQuery by remember { mutableStateOf("") }

    // Filtrado de usuarios
    val filteredUsuarios = remember(searchQuery, usuarios) {
        usuarios.filter { usuario ->
            val query = searchQuery.trim().lowercase()
            query.isEmpty() ||
                usuario.nombreUsuario?.lowercase()?.contains(query) == true ||
                usuario.apellidoPaternoUsuario?.lowercase()?.contains(query) == true ||
                usuario.apellidoMaternoUsuario?.lowercase()?.contains(query) == true ||
                usuario.curpUsuario?.lowercase()?.contains(query) == true ||
                usuario.correo?.lowercase()?.contains(query) == true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Buscador
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Buscar usuario") },
            placeholder = { Text("Nombre, correo o CURP") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
        Box(modifier = Modifier.weight(1f)) {
            when {
                error != null -> {
                    ErrorMessage(error)
                }
                usuarios.isEmpty() -> {
                    LoadingIndicator()
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 0.dp, vertical = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(filteredUsuarios) { usuario ->
                            UsuarioCardHistorialStyle(
                                usuario = usuario,
                                onDelete = { viewModel.eliminarUsuario(it.id ?: "") }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminNavHost() {
    val navController = rememberNavController()
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("inicio", "usuarios", "datos", "reportes")

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") },
                    selected = selectedTab == 0,
                    onClick = {
                        selectedTab = 0
                        navController.navigate("inicio") { launchSingleTop = true }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Person, contentDescription = "Usuarios") },
                    label = { Text("Usuarios") },
                    selected = selectedTab == 1,
                    onClick = {
                        selectedTab = 1
                        navController.navigate("usuarios") { launchSingleTop = true }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.List, contentDescription = "Reportes") },
                    label = { Text("Reportes") },
                    selected = selectedTab == 2,
                    onClick = {
                        selectedTab = 2
                        navController.navigate("reportes") { launchSingleTop = true }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.ShoppingCart, contentDescription = "Pagos") },
                    label = { Text("Pagos") },
                    selected = selectedTab == 3,
                    onClick = {
                        selectedTab = 3
                        navController.navigate("pagos") { launchSingleTop = true }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Info, contentDescription = "Datos") },
                    label = { Text("Datos") },
                    selected = selectedTab == 4,
                    onClick = {
                        selectedTab = 4
                        navController.navigate("datos") { launchSingleTop = true }
                    }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "inicio",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("inicio") { AdminInicioScreen() }
            composable("usuarios") { AdminUsuariosScreen() }
            composable("datos") { AdminDatosScreen() }
            composable("reportes") { AdminReportesScreen() }
            composable("pagos") { AdminPagosScreen() }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun AdminHomePageScreenPreview() {
    val navController = rememberNavController()
    AdminTheme {
        AdminHomePageScreenContent(navController = navController)
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun AdminDatosScreenPreview() {
    val navController = rememberNavController()
    AdminTheme {
        AdminDatosScreen(navController = navController)
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun AdminReportesScreenPreview() {
    AdminTheme {
        AdminReportesScreen()
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun AdminUsuariosScreenPreview() {
    AdminTheme {
        AdminUsuariosScreen()
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun AdminNavHostPreview() {
    AdminTheme {
        AdminNavHost()
    }
}

@Composable
fun CanasDeUsuarioScreen(usuario: UserResponse, onClose: () -> Unit) {
    val scope = rememberCoroutineScope()
    var canas by remember { mutableStateOf<List<CanaDto>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(usuario.id) {
        isLoading = true
        error = null
        scope.launch {
            try {
                canas = RetrofitClient.canaService.getAllCanaByUsuario(usuario.id ?: "")
            } catch (e: Exception) {
                error = "Error al cargar cañas: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onClose) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
            }
            Text(
                text = "Cañas de ${usuario.nombreUsuario}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Divider(modifier = Modifier.padding(vertical = 8.dp))
        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            error != null -> {
                Text(
                    text = error!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            canas.isEmpty() -> {
                Text(
                    text = "No hay cañas registradas para este usuario",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            else -> {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(canas) { cana ->
                        ExpandableCanaCard(cana)
                    }
                }
            }
        }
    }
}

@Composable
fun PagosPendientesSection() {
    val viewModel: PagosViewModel = viewModel()
    val state by viewModel.state.collectAsState(initial = PagosState.Empty)

    LaunchedEffect(Unit) {
        viewModel.cargarPagosPendientesMes()
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Pagos pendientes del mes",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            when (state) {
                is PagosState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                is PagosState.Error -> {
                    Text(
                        text = (state as PagosState.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                is PagosState.Empty -> {
                    Text(
                        text = "Todo bien hasta el momento...",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                is PagosState.Success -> {
                    val pagos = (state as PagosState.Success).pagos
                    pagos.forEach { pago ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Usuario: ${pago.idUsuario}", fontWeight = FontWeight.SemiBold)
                                Text("Monto: $${pago.monto} MXN")
                                Text("Periodo: ${pago.periodoInicio} a ${pago.periodoFin}")
                            }
                            Button(
                                onClick = { viewModel.marcarComoPagado(pago) },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Text("Marcar como pagado")
                            }
                        }
                        Divider()
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminDashboardResumen() {
    var totalPagosPendientes by remember { mutableStateOf(0) }
    var totalUsuarios by remember { mutableStateOf(0) }
    var totalCanasHoy by remember { mutableStateOf(0) }
    var montoTotalPendiente by remember { mutableStateOf(0.0) }
    val scope = rememberCoroutineScope()
    val hoy = LocalDate.now().toString()

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val pagos = RetrofitClient.pagoService.getPagos()
                totalPagosPendientes = pagos.count { it.estado == "pendiente" }
                montoTotalPendiente = pagos.filter { it.estado == "pendiente" }.sumOf { it.monto }
                val usuarios = RetrofitClient.usuarioService.getAll()
                totalUsuarios = usuarios.size
                val canas = RetrofitClient.canaService.getCanaByFecha(hoy)
                totalCanasHoy = canas.size
            } catch (_: Exception) {}
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Resumen rápido", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text("Pagos pendientes: $totalPagosPendientes")
            Text("Monto total pendiente: $${montoTotalPendiente} MXN")
            Text("Usuarios registrados: $totalUsuarios")
            Text("Cañas registradas hoy: $totalCanasHoy")
        }
    }
} 