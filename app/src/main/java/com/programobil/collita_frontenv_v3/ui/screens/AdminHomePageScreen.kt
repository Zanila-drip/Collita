package com.programobil.collita_frontenv_v3.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import androidx.compose.material.icons.filled.Add
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

@Composable
fun AdminHomePageScreen(navController: NavController) {
    val viewModel: AdminViewModel = viewModel()
    val usuarios = viewModel.usuarios
    val error = viewModel.error
    var searchQuery by remember { mutableStateOf("") }

    // Obtener el nombre del admin logueado (primer usuario si es Ramiro, o puedes pasar el nombre por parámetro si lo prefieres)
    val admin = usuarios.firstOrNull { it.correo?.contains("ramiro", ignoreCase = true) == true }
    val adminName = admin?.let { "${it.nombreUsuario} ${it.apellidoPaternoUsuario}" } ?: "Administrador"
    val isAdmin = admin != null

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

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Fila con saludo y botón de reportes
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 24.dp, end = 16.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Bienvenido $adminName",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                if (isAdmin) {
                    IconButton(onClick = { navController.navigate("reportes") }) {
                        Icon(
                            imageVector = Icons.Filled.List,
                            contentDescription = "Ir a reportes",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            Divider(modifier = Modifier.padding(bottom = 8.dp))
            // Buscador
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Buscar usuario") },
                placeholder = { Text("Nombre, correo o CURP") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
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
                                UsuarioCardHistorialStyle(usuario = usuario)
                            }
                        }
                    }
                }
            }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UsuarioCardHistorialStyle(usuario: UserResponse) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { expanded = !expanded },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Nombre y botón editar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "Usuario",
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "${usuario.nombreUsuario} ${usuario.apellidoPaternoUsuario} ${usuario.apellidoMaternoUsuario}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { /* TODO: Editar */ }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Divider(color = MaterialTheme.colorScheme.outlineVariant)
            // CURP
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "CURP",
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "CURP: ${usuario.curpUsuario ?: "-"}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            // Teléfono
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Phone,
                    contentDescription = "Teléfono",
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Teléfono: ${usuario.telefono ?: "-"}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            // Correo
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Email,
                    contentDescription = "Correo",
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Correo: ${usuario.correo ?: "-"}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            // Acciones rápidas (solo si está expandido)
            if (expanded) {
                Divider(color = MaterialTheme.colorScheme.outlineVariant)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { /* TODO: Tickets */ },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.List,
                            contentDescription = "Tickets",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Tickets")
                    }
                    OutlinedButton(
                        onClick = { /* TODO: Pagar */ },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Pagar",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Pagar")
                    }
                }
            }
        }
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
        Text(
            text = "Cerrar sesión",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier
                .align(Alignment.End)
                .clickable {
                    navController?.navigate("login") {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
        )
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

    LaunchedEffect(formattedDate) {
        isLoading = true
        try {
            canas = RetrofitClient.canaService.getCanaByFecha(formattedDate)
            error = null
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
                        text = "Total de cañas: ${canas.size}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Total de arañazos: ${canas.sumOf { it.cantidadCanaUsuario }}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Promedio por caña: ${if (canas.isNotEmpty()) canas.sumOf { it.cantidadCanaUsuario } / canas.size else 0}",
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
    val scope = rememberCoroutineScope()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                expanded = !expanded
                if (expanded && usuario == null) {
                    scope.launch {
                        try {
                            usuario = RetrofitClient.usuarioService.getUsuarioById(cana.idUsuario)
                        } catch (_: Exception) {}
                    }
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
                    text = usuario?.nombreUsuario ?: "Cargando...",
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
                Text("CURP: ${usuario?.curpUsuario ?: "Cargando..."}")
                Text("ID: ${cana.idUsuario}")
                Text("Hora inicio: ${cana.horaInicioUsuario}")
                Text("Hora fin: ${cana.horaFinalUsuario}")
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
                            UsuarioCardHistorialStyle(usuario = usuario)
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
                    icon = { Icon(Icons.Filled.Info, contentDescription = "Datos") },
                    label = { Text("Datos") },
                    selected = selectedTab == 2,
                    onClick = {
                        selectedTab = 2
                        navController.navigate("datos") { launchSingleTop = true }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.List, contentDescription = "Reportes") },
                    label = { Text("Reportes") },
                    selected = selectedTab == 3,
                    onClick = {
                        selectedTab = 3
                        navController.navigate("reportes") { launchSingleTop = true }
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
        }
    }
} 