/* Aplicacion Collita v1
 Participantes: Godos García Jesús Emmanuel 217o02950,
                Ortiz Sánchez Néstor Éibar 217o03062,
                Axel David Ruiz Vargas 217o03139,
                Ramiro Aguilar Morales 207o02190*/
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