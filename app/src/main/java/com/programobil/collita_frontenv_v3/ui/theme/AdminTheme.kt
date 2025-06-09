/* Aplicacion Collita v1
 Participantes: Godos García Jesús Emmanuel 217o02950,
                Ortiz Sánchez Néstor Éibar 217o03062,
                Axel David Ruiz Vargas 217o03139,
                Ramiro Aguilar Morales 207o02190*/
package com.programobil.collita_frontenv_v3.ui.theme


import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Colores principales
val AdminPrimary = Color(0xFF424242) // Gris oscuro
val AdminSecondary = Color(0xFF757575) // Gris medio
val AdminAccent = Color(0xFF2196F3) // Azul
val AdminBackground = Color(0xFFF5F5F5) // Gris claro
val AdminError = Color(0xFFB00020)

// Esquema de colores
val AdminColorScheme = lightColorScheme(
    primary = AdminPrimary,
    onPrimary = Color.White,
    secondary = AdminSecondary,
    onSecondary = Color.White,
    background = AdminBackground,
    onBackground = AdminPrimary,
    surface = Color.White,
    onSurface = AdminPrimary,
    error = AdminError,
    onError = Color.White,
    surfaceVariant = Color(0xFFE0E0E0), // Gris más claro para variantes de superficie
    onSurfaceVariant = AdminPrimary,
    primaryContainer = Color(0xFFBDBDBD), // Gris medio para contenedores
    onPrimaryContainer = AdminPrimary
)

@Composable
fun AdminTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AdminColorScheme,
        typography = MaterialTheme.typography,
        content = content
    )
} 