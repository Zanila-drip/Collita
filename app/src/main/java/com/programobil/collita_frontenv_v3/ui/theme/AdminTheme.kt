package com.programobil.collita_frontenv_v3.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val AdminPrimary = Color(0xFF0D1B2A) // Azul marino
val AdminSecondary = Color(0xFF1B263B) // Gris azulado oscuro
val AdminAccent = Color(0xFFFFD700) // Dorado
val AdminOnPrimary = Color.LightGray
val AdminError = Color(0xFFB00020)

val AdminColorScheme = darkColorScheme(
    primary = AdminPrimary,
    onPrimary = AdminOnPrimary,
    secondary = AdminSecondary,
    onSecondary = AdminOnPrimary,
    background = AdminSecondary,
    onBackground = AdminOnPrimary,
    surface = AdminPrimary,
    onSurface = AdminAccent,
    error = AdminError,
    onError = Color.LightGray
)

@Composable
fun AdminTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AdminColorScheme,
        typography = MaterialTheme.typography,
        content = content
    )
} 