/* Aplicacion Collita v1
 Participantes: Godos García Jesús Emmanuel 217o02950,
                Ortiz Sánchez Néstor Éibar 217o03062,
                Peña Perez Axel  217o00677,
                Axel David Ruiz Vargas 217o03139,
                Ramiro Morales 207o02190*/

/**
 * LoadingIndicator - Componente de carga
 * 
 * Descripción:
 * Componente reutilizable que muestra un indicador de carga
 * durante operaciones asíncronas.
 * 
 * Características:
 * - Animación circular de carga
 * - Mensaje personalizable
 * - Fondo semitransparente
 * - Centrado en pantalla
 * 
 * Parámetros:
 * - message: String - Mensaje a mostrar durante la carga
 * - modifier: Modifier - Modificadores de estilo y layout
 * 
 * Uso:
 * Se utiliza en pantallas que realizan operaciones
 * asíncronas como:
 * - Login
 * - Registro de cañas
 * - Carga de datos
 * - Subida de imágenes
 * 
 * Ejemplo:
 * LoadingIndicator(
 *     message = "Cargando datos...",
 *     modifier = Modifier.fillMaxSize()
 * )
 */

package com.programobil.collita_frontenv_v3.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme

@Composable
fun LoadingIndicator() {
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