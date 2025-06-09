/* Aplicacion Collita v1
 Participantes: Godos García Jesús Emmanuel 217o02950,
                Ortiz Sánchez Néstor Éibar 217o03062,
                Axel David Ruiz Vargas 217o03139,
                Ramiro Aguilar Morales 207o02190*/
/**
 * AdminUsuariosScreen - Pantalla de gestión de usuarios
 * 
 * Flujo de la pantalla:
 * 1. Al cargar, se obtiene la lista de usuarios registrados
 * 2. Se muestran los usuarios con:
 *    - Información básica
 *    - Estado de cuenta
 *    - Estadísticas de actividad
 * 
 * Componentes principales:
 * - Barra de búsqueda:
 *    * Por nombre
 *    * Por CURP
 *    * Por correo
 * - Lista de usuarios con:
 *    * Nombre completo
 *    * CURP
 *    * Correo
 *    * Estado
 *    * Fecha de registro
 *    * Total de cañas
 *    * Total de pagos
 * - Botones de acción:
 *    * Agregar usuario
 *    * Editar usuario
 *    * Desactivar/Activar
 * 
 * Interacciones:
 * - Al hacer clic en un usuario, se muestra su perfil completo
 * - Al hacer clic en editar, se abre el formulario de edición
 * - Al hacer clic en ver cañas, se muestra el historial de cañas
 * - Al hacer clic en ver pagos, se muestra el historial de pagos
 * - Al usar la búsqueda, se filtra la lista de usuarios
 */

package com.programobil.collita_frontenv_v3.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.programobil.collita_frontenv_v3.ui.viewmodel.AdminViewModel
import com.programobil.collita_frontenv_v3.data.api.UserResponse
import com.programobil.collita_frontenv_v3.ui.components.UsuarioCardHistorialStyle
import com.programobil.collita_frontenv_v3.ui.components.ErrorMessage
import com.programobil.collita_frontenv_v3.ui.components.LoadingIndicator

@Composable
fun AdminUsuariosScreen(
    usuarios: List<UserResponse>,
    error: String?,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onDeleteUsuario: (UserResponse) -> Unit,
    onShowCanas: (UserResponse) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            label = { Text("Buscar usuario") },
            placeholder = { Text("Nombre, correo o CURP") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
        )
        Box(modifier = Modifier.weight(1f)) {
            when {
                error != null -> {
                    ErrorMessage(error)
                }
                usuarios.isEmpty() -> {
                    LoadingIndicator()
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 0.dp, vertical = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(usuarios) { usuario ->
                            UsuarioCardHistorialStyle(
                                usuario = usuario,
                                onDelete = { onDeleteUsuario(usuario) },
                                onShowCanas = { onShowCanas(usuario) }
                            )
                        }
                    }
                }
            }
        }
    }
} 