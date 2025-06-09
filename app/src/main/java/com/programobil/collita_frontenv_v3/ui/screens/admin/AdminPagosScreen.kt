package com.programobil.collita_frontenv_v3.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.programobil.collita_frontenv_v3.ui.viewmodel.PagosViewModel
import com.programobil.collita_frontenv_v3.ui.viewmodel.PagosState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.Dialog
import com.programobil.collita_frontenv_v3.data.api.UsuarioDto
import com.programobil.collita_frontenv_v3.data.api.CanaDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import coil.compose.AsyncImage
import androidx.compose.foundation.clickable
import com.programobil.collita_frontenv_v3.data.api.PagoDto
import com.programobil.collita_frontenv_v3.network.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import android.app.DatePickerDialog
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

@Composable
fun AdminPagosScreen() {
    val viewModel: PagosViewModel = viewModel()
    val state by viewModel.state.collectAsState(initial = PagosState.Empty)
    val scope = rememberCoroutineScope()
    var selectedPago by remember { mutableStateOf<PagoDto?>(null) }
    var usuarioDetalle by remember { mutableStateOf<UsuarioDto?>(null) }
    var canasDetalle by remember { mutableStateOf<List<CanaDto>>(emptyList()) }
    var isDetalleLoading by remember { mutableStateOf(false) }
    var detalleError by remember { mutableStateOf<String?>(null) }
    var fechaPagoEdit by remember { mutableStateOf<LocalDate?>(null) }

    LaunchedEffect(Unit) {
        viewModel.cargarPagosPendientesMes()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Gestión de Pagos",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
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
                                    .padding(vertical = 8.dp)
                                    .clickable {
                                        selectedPago = pago
                                        isDetalleLoading = true
                                        detalleError = null
                                        usuarioDetalle = null
                                        canasDetalle = emptyList()
                                        // Cargar usuario y cañas asociadas
                                        scope.launch {
                                            try {
                                                usuarioDetalle = withContext(Dispatchers.IO) {
                                                    RetrofitClient.usuarioService.getUsuarioById(pago.idUsuario)
                                                }
                                                canasDetalle = pago.detalleCanaIds?.mapNotNull { canaId ->
                                                    try {
                                                        RetrofitClient.canaService.getCanaById(canaId)
                                                    } catch (e: Exception) { null }
                                                } ?: emptyList()
                                            } catch (e: Exception) {
                                                detalleError = "No se pudo cargar el detalle"
                                            } finally {
                                                isDetalleLoading = false
                                            }
                                        }
                                    },
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

    // Diálogo de detalle de pago
    if (selectedPago != null) {
        Dialog(onDismissRequest = { selectedPago = null }) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Detalle de pago", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    if (isDetalleLoading) {
                        CircularProgressIndicator()
                    } else if (detalleError != null) {
                        Text(detalleError!!, color = MaterialTheme.colorScheme.error)
                    } else {
                        val context = LocalContext.current
                        val fechaActual = fechaPagoEdit ?: selectedPago?.fechaPago?.let { LocalDate.parse(it) } ?: LocalDate.now()
                        OutlinedTextField(
                            value = fechaActual.format(DateTimeFormatter.ISO_DATE),
                            onValueChange = {},
                            label = { Text("Fecha de cobro") },
                            readOnly = true,
                            trailingIcon = {
                                IconButton(onClick = {
                                    val date = fechaActual
                                    DatePickerDialog(
                                        context,
                                        { _, year, month, dayOfMonth ->
                                            fechaPagoEdit = LocalDate.of(year, month + 1, dayOfMonth)
                                        },
                                        date.year, date.monthValue - 1, date.dayOfMonth
                                    ).show()
                                }) {
                                    Icon(Icons.Default.DateRange, contentDescription = "Seleccionar fecha")
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(8.dp))
                        Text("Usuario: " + (usuarioDetalle?.let { it.nombreUsuario + " " + (it.apellidoPaternoUsuario ?: "") + " " + (it.apellidoMaternoUsuario ?: "") } ?: selectedPago?.idUsuario ?: ""))
                        Text("ID pago: ${selectedPago?.id ?: ""}")
                        Text("Monto: $${selectedPago?.monto ?: ""} MXN")
                        Text("Periodo: ${selectedPago?.periodoInicio ?: ""} a ${selectedPago?.periodoFin ?: ""}")
                        Text("Estado: ${selectedPago?.estado ?: ""}")
                        Spacer(Modifier.height(8.dp))
                        if (canasDetalle.isNotEmpty()) {
                            Text("Cañas asociadas:", fontWeight = FontWeight.Bold)
                            canasDetalle.forEach { cana ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        if (!cana.imagenPath.isNullOrEmpty()) {
                                            AsyncImage(
                                                model = "http://192.168.1.71:8080" + cana.imagenPath,
                                                contentDescription = "Imagen de caña",
                                                modifier = Modifier.size(48.dp)
                                            )
                                        }
                                        Spacer(Modifier.width(8.dp))
                                        Column {
                                            Text("Resumen: ${cana.resumenCosecha ?: "Sin resumen"}")
                                            Text("Cantidad: ${cana.cantidadCanaUsuario}")
                                        }
                                    }
                                }
                            }
                        } else {
                            Text("No hay cañas asociadas")
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        Button(onClick = {
                            // Guardar cambios de fecha de cobro
                            val pago = selectedPago
                            val nuevaFecha = fechaPagoEdit?.toString() ?: pago?.fechaPago
                            if (pago != null && nuevaFecha != null) {
                                val pagoActualizado = pago.copy(fechaPago = nuevaFecha)
                                scope.launch {
                                    try {
                                        RetrofitClient.pagoService.actualizarPago(pagoActualizado.id!!, pagoActualizado)
                                        selectedPago = null
                                        fechaPagoEdit = null
                                        // Opcional: recargar lista de pagos
                                        viewModel.cargarPagosPendientesMes()
                                    } catch (_: Exception) {}
                                }
                            }
                        }) {
                            Text("Guardar")
                        }
                        Spacer(Modifier.width(8.dp))
                        Button(onClick = { selectedPago = null }) {
                            Text("Cerrar")
                        }
                    }
                }
            }
        }
    }
} 