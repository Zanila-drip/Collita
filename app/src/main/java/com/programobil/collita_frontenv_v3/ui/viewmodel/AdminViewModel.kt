/* Aplicacion Collita v1
 Participantes: Godos García Jesús Emmanuel 217o02950,
                Ortiz Sánchez Néstor Éibar 217o03062,
                Peña Perez Axel  217o00677,
                Axel David Ruiz Vargas 217o03139,
                Ramiro Morales 207o02190*/

/**
 * AdminViewModel - ViewModel para la gestión administrativa
 * 
 * Responsabilidades:
 * 1. Gestión de datos administrativos:
 *    - Estadísticas generales
 *    - Lista de usuarios
 *    - Lista de pagos
 *    - Reportes
 * 
 * Estados manejados:
 * - Estadísticas:
 *    * Total usuarios
 *    * Total pagos pendientes
 *    * Monto total pendiente
 *    * Cañas registradas hoy
 * - Listas:
 *    * Usuarios activos
 *    * Pagos pendientes
 *    * Reportes generados
 * 
 * Funciones principales:
 * - Cargar estadísticas
 * - Gestionar usuarios
 * - Gestionar pagos
 * - Generar reportes
 * - Filtrar y buscar datos
 * 
 * Eventos:
 * - Actualización de estadísticas
 * - Cambios en usuarios
 * - Cambios en pagos
 * - Generación de reportes
 */

package com.programobil.collita_frontenv_v3.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.programobil.collita_frontenv_v3.network.RetrofitClient
import com.programobil.collita_frontenv_v3.data.api.UserResponse
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class AdminViewModel : ViewModel() {
    var usuarios by mutableStateOf<List<UserResponse>>(emptyList())
        private set

    var error by mutableStateOf<String?>(null)
        private set

    init {
        cargarUsuarios()
    }

    fun cargarUsuarios() {
        viewModelScope.launch {
            try {
                usuarios = RetrofitClient.apiService.getAllUsuarios()
                error = null
            } catch (e: Exception) {
                error = "Error al cargar usuarios: ${e.message}"
            }
        }
    }

    fun eliminarUsuario(id: String) {
        viewModelScope.launch {
            try {
                RetrofitClient.apiService.deleteUsuario(id)
                RetrofitClient.canaService.deleteCanasByUsuario(id)
                usuarios = usuarios.filter { it.id != id }
            } catch (e: Exception) {
                error = "Error al eliminar usuario: ${e.message}"
            }
        }
    }
} 