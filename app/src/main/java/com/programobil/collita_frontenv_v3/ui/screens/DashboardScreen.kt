package com.programobil.collita_frontenv_v3.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    userName: String,
    navController: NavController
) {
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf("Principal", "Cosecha", "Historial", "Datos")
    
    // Estado de ejemplo (esto vendría del ViewModel)
    var hasActiveSession by remember { mutableStateOf(false) }
    var hasActionsToday by remember { mutableStateOf(false) }
    var lastAction by remember { mutableStateOf("08:00 - 12:00, 2 toneladas") }
    var totalCanaToday by remember { mutableStateOf(2.5) }
    var numberOfActions by remember { mutableStateOf(1) }
    var timeWorked by remember { mutableStateOf("4 horas") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = "Usuario"
                        )
                        Text("¡Hola $userName!")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = when (index) {
                                    0 -> Icons.Filled.Home
                                    1 -> Icons.Filled.List
                                    2 -> Icons.Filled.DateRange
                                    else -> Icons.Filled.Person
                                },
                                contentDescription = item
                            )
                        },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index }
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Fecha y hora actual
            Text(
                text = LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("EEEE d 'de' MMMM, HH:mm")
                ),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            when (selectedItem) {
                0 -> PrincipalContent(
                    hasActiveSession = hasActiveSession,
                    hasActionsToday = hasActionsToday,
                    lastAction = lastAction,
                    totalCanaToday = totalCanaToday,
                    numberOfActions = numberOfActions,
                    timeWorked = timeWorked,
                    onStartNewAction = { /* TODO: Implementar */ }
                )
                1 -> CosechaContent()
                2 -> HistorialContent()
                3 -> DatosContent()
            }
        }
    }
}

@Composable
private fun PrincipalContent(
    hasActiveSession: Boolean,
    hasActionsToday: Boolean,
    lastAction: String,
    totalCanaToday: Double,
    numberOfActions: Int,
    timeWorked: String,
    onStartNewAction: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Estado actual
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (hasActiveSession) 
                    MaterialTheme.colorScheme.primaryContainer 
                else 
                    MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Estado Actual",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (hasActiveSession) "Sesión activa" else "Sin sesión activa",
                    color = if (hasActiveSession) 
                        MaterialTheme.colorScheme.onPrimaryContainer 
                    else 
                        MaterialTheme.colorScheme.onErrorContainer
                )
                if (hasActionsToday) {
                    Text(
                        text = "Última acción: $lastAction",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        // Botón de nueva acción
        Button(
            onClick = onStartNewAction,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Iniciar Nueva Acción")
        }

        // Resumen del día
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Resumen del Día",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total de caña:")
                    Text("$totalCanaToday toneladas")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Acciones registradas:")
                    Text("$numberOfActions")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Tiempo trabajado:")
                    Text(timeWorked)
                }
            }
        }
    }
}

@Composable
private fun CosechaContent() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Contenido de Cosecha",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun HistorialContent() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Historial de Actividades",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun DatosContent() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Datos del Usuario",
            style = MaterialTheme.typography.bodyLarge
        )
    }
} 