/* Aplicacion Collita v1
 Participantes: Godos García Jesús Emmanuel 217o02950,
                Ortiz Sánchez Néstor Éibar 217o03062,
                Axel David Ruiz Vargas 217o03139,
                Ramiro Aguilar Morales 207o02190*/
/**
 * AdminReportesScreen - Pantalla de reportes y estadísticas
 * 
 * Flujo de la pantalla:
 * 1. Al cargar, se obtienen las estadísticas generales:
 *    - Total de cañas registradas
 *    - Total de pagos realizados
 *    - Monto total cobrado
 *    - Usuarios activos
 * 2. Se muestran los reportes por:
 *    - Período (diario/semanal/mensual)
 *    - Usuario
 *    - Tipo de reporte
 * 
 * Componentes principales:
 * - Selector de período:
 *    * Calendario para selección de fechas
 *    * Filtros predefinidos (hoy/semana/mes)
 * - Gráficos:
 *    * Cañas por día
 *    * Pagos por período
 *    * Distribución por usuario
 * - Tablas de datos:
 *    * Resumen de cañas
 *    * Resumen de pagos
 *    * Top usuarios
 * 
 * Interacciones:
 * - Al seleccionar fechas, se actualizan los reportes
 * - Al hacer clic en un gráfico, se muestra el detalle
 * - Al hacer clic en exportar, se genera PDF/Excel
 * - Al hacer clic en un usuario, se muestra su historial
 */

package com.programobil.collita_frontenv_v3.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import android.app.DatePickerDialog
import androidx.compose.ui.platform.LocalContext
import java.util.Calendar
import com.programobil.collita_frontenv_v3.data.api.CanaDto
import com.programobil.collita_frontenv_v3.network.RetrofitClient
import androidx.compose.ui.text.font.FontWeight
import com.programobil.collita_frontenv_v3.ui.components.ErrorMessage
import com.programobil.collita_frontenv_v3.ui.components.LoadingIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import kotlinx.coroutines.launch
import coil.compose.AsyncImage
import androidx.compose.ui.window.Dialog
import com.programobil.collita_frontenv_v3.data.api.UsuarioDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.foundation.clickable

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
    var selectedCana by remember { mutableStateOf<CanaDto?>(null) }
    var usuarioDetalle by remember { mutableStateOf<UsuarioDto?>(null) }
    var isUsuarioLoading by remember { mutableStateOf(false) }
    var usuarioError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(formattedDate) {
        isLoading = true
        scope.launch {
            try {
                canas = RetrofitClient.canaService.getCanaByFecha(formattedDate)
                error = null
            } catch (e: Exception) {
                error = "Error al cargar los datos: ${e.message}"
            } finally {
                isLoading = false
            }
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
            LoadingIndicator()
        } else if (error != null) {
            ErrorMessage(error!!)
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
                        CanaCard(cana, onClick = {
                            selectedCana = cana
                            isUsuarioLoading = true
                            usuarioError = null
                            scope.launch {
                                try {
                                    usuarioDetalle = withContext(Dispatchers.IO) {
                                        RetrofitClient.usuarioService.getUsuarioById(cana.idUsuario)
                                    }
                                } catch (e: Exception) {
                                    usuarioError = "No se pudo cargar el usuario"
                                    usuarioDetalle = null
                                } finally {
                                    isUsuarioLoading = false
                                }
                            }
                        })
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

        // Diálogo de detalle de caña
        if (selectedCana != null) {
            Dialog(onDismissRequest = { selectedCana = null }) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("Detalle de caña", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                        if (isUsuarioLoading) {
                            CircularProgressIndicator()
                        } else if (usuarioError != null) {
                            Text(usuarioError!!, color = MaterialTheme.colorScheme.error)
                        } else {
                            Text("Usuario: " + (usuarioDetalle?.nombreUsuario ?: selectedCana!!.idUsuario))
                        }
                        Text("ID caña: ${selectedCana!!.id}")
                        Text("ID empleado: ${selectedCana!!.idUsuario}")
                        Text("Cantidad de arañazos: ${selectedCana!!.cantidadCanaUsuario}")
                        Text("Mensaje: ${selectedCana!!.resumenCosecha ?: "Sin mensaje"}")
                        Text("Costo estimado: $${(selectedCana!!.cantidadCanaUsuario * 80).toInt()} MXN")
                        if (!selectedCana!!.imagenPath.isNullOrEmpty()) {
                            Spacer(Modifier.height(8.dp))
                            AsyncImage(
                                model = "http://192.168.1.71:8080" + selectedCana!!.imagenPath,
                                contentDescription = "Imagen de caña",
                                modifier = Modifier.size(180.dp)
                            )
                        }
                        Spacer(Modifier.height(12.dp))
                        Button(onClick = { selectedCana = null }, modifier = Modifier.align(Alignment.End)) {
                            Text("Cerrar")
                        }
                    }
                }
            }
        }
    }
}

// Composable para mostrar la card de una caña
@Composable
fun CanaCard(cana: CanaDto, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!cana.imagenPath.isNullOrEmpty()) {
                AsyncImage(
                    model = "http://192.168.1.71:8080" + cana.imagenPath,
                    contentDescription = "Imagen de caña",
                    modifier = Modifier.size(64.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = cana.resumenCosecha ?: "Sin resumen", fontWeight = FontWeight.Bold)
                Text(text = "Cantidad: ${cana.cantidadCanaUsuario}")
                Text(text = "Hora: ${cana.horaInicioUsuario} - ${cana.horaFinalUsuario ?: ""}")
            }
        }
    }
} 