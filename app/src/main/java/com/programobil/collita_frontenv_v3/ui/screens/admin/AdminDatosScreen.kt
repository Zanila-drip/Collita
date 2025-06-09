package com.programobil.collita_frontenv_v3.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.programobil.collita_frontenv_v3.ui.viewmodel.AdminViewModel
import androidx.compose.ui.text.font.FontWeight

@Composable
fun AdminDatosScreen(navController: androidx.navigation.NavController? = null) {
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