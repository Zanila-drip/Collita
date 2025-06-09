/* Aplicacion Collita v1
 Participantes: Godos García Jesús Emmanuel 217o02950,
                Ortiz Sánchez Néstor Éibar 217o03062,
                Axel David Ruiz Vargas 217o03139,
                Ramiro Aguilar Morales 207o02190*/

/**
 * CanasDeUsuarioScreen - Pantalla de visualización de cañas por usuario
 * 
 * Flujo de la pantalla:
 * 1. Al cargar, se obtienen las cañas del usuario seleccionado
 * 2. Se muestran las cañas organizadas por:
 *    - Fecha
 *    - Estado
 *    - Monto
 * 
 * Componentes principales:
 * - Información del usuario:
 *    * Nombre completo
 *    * CURP
 *    * Correo
 * - Filtros:
 *    * Por fecha
 *    * Por estado
 *    * Por monto
 * - Lista de cañas con:
 *    * Fecha y hora
 *    * Cantidad
 *    * Imagen
 *    * Resumen
 *    * Estado
 *    * Monto
 * 
 * Interacciones:
 * - Al hacer clic en una caña, se muestra el detalle completo
 * - Al hacer clic en la imagen, se muestra en tamaño completo
 * - Al usar los filtros, se actualiza la lista de cañas
 * - Al hacer clic en exportar, se genera un reporte
 */

package com.programobil.collita_frontenv_v3.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.programobil.collita_frontenv_v3.data.api.UserResponse
import com.programobil.collita_frontenv_v3.data.api.CanaDto
import com.programobil.collita_frontenv_v3.network.RetrofitClient
import androidx.compose.ui.text.font.FontWeight
import com.programobil.collita_frontenv_v3.ui.components.ErrorMessage
import com.programobil.collita_frontenv_v3.ui.components.LoadingIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import kotlinx.coroutines.launch

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
                LoadingIndicator()
            }
            error != null -> {
                ErrorMessage(error!!)
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
                        Text(text = cana.resumenCosecha ?: "Sin resumen")
                    }
                }
            }
        }
    }
} 