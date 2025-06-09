/* Aplicacion Collita v1
 Participantes: Godos García Jesús Emmanuel 217o02950,
                Ortiz Sánchez Néstor Éibar 217o03062,
                Peña Perez Axel  217o00677,
                Axel David Ruiz Vargas 217o03139,
                Ramiro Morales 207o02190*/

/**
 * CanaViewModel - ViewModel para la gestión de cañas
 * 
 * Responsabilidades:
 * 1. Gestión de datos de cañas:
 *    - Registro de nuevas cañas
 *    - Consulta de cañas existentes
 *    - Actualización de cañas
 *    - Eliminación de cañas
 * 
 * Estados manejados:
 * - Cañas:
 *    * Lista de cañas del usuario
 *    * Caña actual en edición
 *    * Estado de carga
 *    * Errores
 * - Filtros:
 *    * Fecha
 *    * Estado
 *    * Cantidad
 * 
 * Funciones principales:
 * - Registrar nueva caña
 * - Cargar cañas por fecha
 * - Cargar cañas por usuario
 * - Actualizar caña existente
 * - Eliminar caña
 * - Subir imagen
 * 
 * Eventos:
 * - Registro exitoso
 * - Error en registro
 * - Actualización de lista
 * - Cambio en filtros
 */

package com.programobil.collita_frontenv_v3.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.programobil.collita_frontenv_v3.data.api.CanaDto
import com.programobil.collita_frontenv_v3.data.api.CanaApi
import com.programobil.collita_frontenv_v3.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

sealed class CanaState {
    object Initial : CanaState()
    object Loading : CanaState()
    data class Success(val canas: List<CanaDto>) : CanaState()
    data class Error(val message: String) : CanaState()
}

class CanaViewModel : ViewModel() {
    private val canaService: CanaApi = RetrofitClient.canaService

    private val _state = MutableStateFlow<CanaState>(CanaState.Initial)
    val state: StateFlow<CanaState> = _state

    fun createCana(canaDto: CanaDto) {
        viewModelScope.launch {
            try {
                _state.value = CanaState.Loading
                val response = canaService.createCana(canaDto)
                // Actualizar la lista de cañas después de crear una nueva
                getCanaByUsuario(canaDto.idUsuario, canaDto.fecha.toString())
            } catch (e: Exception) {
                _state.value = CanaState.Error(e.message ?: "Error al crear la caña")
            }
        }
    }

    fun getCanaByUsuario(idUsuario: String, fecha: String) {
        viewModelScope.launch {
            try {
                _state.value = CanaState.Loading
                val response = canaService.getCanaByUsuario(idUsuario, fecha)
                _state.value = CanaState.Success(response)
            } catch (e: Exception) {
                _state.value = CanaState.Error(e.message ?: "Error al obtener las cañas")
            }
        }
    }
} 