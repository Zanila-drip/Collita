package com.programobil.collita_frontenv_v3.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.programobil.collita_frontenv_v3.data.api.CanaDto
import com.programobil.collita_frontenv_v3.ui.viewmodel.HistorialCanaViewModel
import com.programobil.collita_frontenv_v3.ui.viewmodel.HistorialCanaState
import androidx.compose.foundation.clickable
import androidx.compose.animation.animateContentSize
import java.time.Duration
import java.time.LocalTime
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.text.style.TextAlign
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import java.text.Normalizer
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController

@Composable
fun HistorialCanaScreen(viewModel: HistorialCanaViewModel, idUsuario: String) {
    val state by viewModel.state.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    // Cargar historial al entrar
    androidx.compose.runtime.LaunchedEffect(idUsuario) {
        viewModel.cargarHistorial(idUsuario)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (state) {
            is HistorialCanaState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is HistorialCanaState.Error -> {
                Text(
                    text = (state as HistorialCanaState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            is HistorialCanaState.Empty -> {
                Text(
                    text = "No hay cañas registradas.",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            is HistorialCanaState.Success -> {
                val canas = (state as HistorialCanaState.Success).canas
                val filteredCanas = canas.filter { cana ->
                    val query = searchQuery.trim().lowercase()
                    // Función para quitar acentos
                    fun quitarAcentos(texto: String): String =
                        Normalizer.normalize(texto, Normalizer.Form.NFD)
                            .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
                    // Día de la semana de la caña
                    val diaSemana = try {
                        LocalDate.parse(cana.fecha).dayOfWeek.getDisplayName(TextStyle.FULL, Locale("es", "ES"))
                    } catch (e: Exception) {
                        ""
                    }
                    val diaSemanaSinAcento = quitarAcentos(diaSemana).lowercase()
                    val querySinAcento = quitarAcentos(query)
                    query.isEmpty() ||
                        cana.fecha.lowercase().contains(query) ||
                        (cana.id ?: "").lowercase().contains(query) ||
                        (cana.resumenCosecha ?: "").lowercase().contains(query) ||
                        diaSemanaSinAcento.contains(querySinAcento)
                }
                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Buscar por fecha, ID o descripción") },
                        placeholder = { Text("Ej: 2024-06-10, 60d1f..., Pig 1", fontSize = MaterialTheme.typography.bodySmall.fontSize) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "Puedes buscar por fecha (2024-06-10), por ID (60d1f...) o por descripción (Pig 1)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 2.dp, bottom = 8.dp).fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(filteredCanas) { cana ->
                            CanaCard(cana)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CanaCard(cana: CanaDto) {
    var expanded by remember { mutableStateOf(false) }
    // Calcular tiempo total
    val tiempoTotal = try {
        if (!cana.horaInicioUsuario.isNullOrBlank() && !cana.horaFinalUsuario.isNullOrBlank()) {
            val inicio = LocalTime.parse(cana.horaInicioUsuario)
            val fin = LocalTime.parse(cana.horaFinalUsuario)
            val duracion = Duration.between(inicio, fin)
            val horas = duracion.toHours()
            val minutos = duracion.toMinutes() % 60
            val segundos = duracion.seconds % 60
            "%dh %dm %ds".format(horas, minutos, segundos)
        } else {
            null
        }
    } catch (e: Exception) {
        null
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .animateContentSize(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Día de la semana (colapsado)
            val diaSemana = try {
                LocalDate.parse(cana.fecha).dayOfWeek.getDisplayName(TextStyle.FULL, Locale("es", "ES")).replaceFirstChar { it.uppercase() }
            } catch (e: Exception) {
                cana.fecha
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = "Día de la semana",
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = diaSemana,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Divider(color = MaterialTheme.colorScheme.outlineVariant)

            // Cantidad
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Cantidad",
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Cantidad: ${cana.cantidadCanaUsuario}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            // Tiempo total (en colapsado)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = "Tiempo total",
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Tiempo total: ${tiempoTotal ?: "-"}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            // Detalles extra solo si está expandido
            if (expanded) {
                Divider(color = MaterialTheme.colorScheme.outlineVariant)
                // Fecha completa
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = "Fecha completa",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = cana.fecha,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                // Horas de inicio y fin
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = "Horas",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Inicio: ${cana.horaInicioUsuario}  Fin: ${cana.horaFinalUsuario}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                // ID de la caña
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = "ID",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "ID: ${cana.id ?: "-"}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                // Resumen
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = "Resumen",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = cana.resumenCosecha?.ifBlank { "Sin resumen" } ?: "Sin resumen",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun HistorialCanaScreenPreview() {
    val fakeViewModel = HistorialCanaViewModel()
    MaterialTheme {
        HistorialCanaScreen(viewModel = fakeViewModel, idUsuario = "usuario_demo")
    }
} 