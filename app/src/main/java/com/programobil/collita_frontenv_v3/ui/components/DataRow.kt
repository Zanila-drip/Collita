/* Aplicacion Collita v1
 Participantes: Godos García Jesús Emmanuel 217o02950,
                Ortiz Sánchez Néstor Éibar 217o03062,
                Peña Perez Axel  217o00677,
                Axel David Ruiz Vargas 217o03139,
                Ramiro Morales 207o02190*/

/**
 * DataRow - Componente de fila de datos
 *
 * Descripción:
 * Componente reutilizable para mostrar un par etiqueta-valor en una fila.
 *
 * Características:
 * - Muestra dos textos alineados horizontalmente
 * - Etiqueta y valor con estilos diferenciados
 * - Espaciado configurable
 * - Adaptable a diferentes pantallas
 *
 * Parámetros:
 * - label: String - Etiqueta o título de la fila
 * - value: String - Valor asociado a la etiqueta
 *
 * Uso:
 * Se utiliza en:
 * - Detalles de usuario
 * - Detalles de caña
 * - Detalles de pago
 * - Listados personalizados
 *
 * Ejemplo:
 * DataRow(
 *     label = "CURP",
 *     value = "XXXX000000HDFRRR01"
 * )
 */

package com.programobil.collita_frontenv_v3.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DataRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
    }
} 