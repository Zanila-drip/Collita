/* Aplicacion Collita v1
 Participantes: Godos García Jesús Emmanuel 217o02950,
                Ortiz Sánchez Néstor Éibar 217o03062,
                Peña Perez Axel  217o00677,
                Axel David Ruiz Vargas 217o03139,
                Ramiro Morales 207o02190*/

/**
 * ErrorMessage - Componente de mensaje de error
 * 
 * Descripción:
 * Componente reutilizable que muestra mensajes de error
 * de forma consistente en toda la aplicación.
 * 
 * Características:
 * - Diseño de tarjeta con borde rojo
 * - Icono de error
 * - Mensaje personalizable
 * - Botón de acción opcional
 * - Animación de entrada
 * 
 * Parámetros:
 * - message: String - Mensaje de error a mostrar
 * - onDismiss: () -> Unit - Función opcional para cerrar el mensaje
 * - modifier: Modifier - Modificadores de estilo y layout
 * 
 * Uso:
 * Se utiliza para mostrar errores en:
 * - Validación de formularios
 * - Errores de red
 * - Errores de autenticación
 * - Errores de operaciones
 * 
 * Ejemplo:
 * ErrorMessage(
 *     message = "Error al cargar los datos",
 *     onDismiss = { /* cerrar mensaje */ },
 *     modifier = Modifier.padding(16.dp)
 * )
 */

package com.programobil.collita_frontenv_v3.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

@Composable
fun ErrorMessage(error: String) {
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