package com.programobil.collita_frontenv_v3.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.programobil.collita_frontenv_v3.data.api.CanaDto
import com.programobil.collita_frontenv_v3.ui.viewmodel.HistorialCanaViewModel
import com.programobil.collita_frontenv_v3.ui.viewmodel.HistorialCanaState

@Composable
fun HistorialCanaScreen(viewModel: HistorialCanaViewModel, idUsuario: String) {
    val state by viewModel.state.collectAsState()

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
                    text = "No hay arañazos registrados.",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            is HistorialCanaState.Success -> {
                val canas = (state as HistorialCanaState.Success).canas
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(canas) { cana ->
                        CanaCard(cana)
                    }
                }
            }
        }
    }
}

@Composable
fun CanaCard(cana: CanaDto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Fecha: ${cana.fecha}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Cantidad: ${cana.cantidadCanaUsuario}")
            Text(text = "Hora inicio: ${cana.horaInicioUsuario}")
            Text(text = "Hora fin: ${cana.horaFinalUsuario}")
            if (!cana.resumenCosecha.isNullOrBlank()) {
                Text(text = "Resumen: ${cana.resumenCosecha}")
            }
        }
    }
} 