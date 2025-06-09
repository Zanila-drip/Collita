/* Aplicacion Collita v1
 Participantes: Godos García Jesús Emmanuel 217o02950,
                Ortiz Sánchez Néstor Éibar 217o03062,
                Axel David Ruiz Vargas 217o03139,
                Ramiro Aguilar Morales 207o02190*/
package com.programobil.collita_frontenv_v3.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.programobil.collita_frontenv_v3.data.api.CanaDto
import com.programobil.collita_frontenv_v3.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class HistorialCanaState {
    object Loading : HistorialCanaState()
    data class Success(val canas: List<CanaDto>) : HistorialCanaState()
    data class Error(val message: String) : HistorialCanaState()
    object Empty : HistorialCanaState()
}

class HistorialCanaViewModel : ViewModel() {
    private val _state = MutableStateFlow<HistorialCanaState>(HistorialCanaState.Empty)
    val state: StateFlow<HistorialCanaState> = _state.asStateFlow()

    fun cargarHistorial(idUsuario: String) {
        _state.value = HistorialCanaState.Loading
        viewModelScope.launch {
            try {
                val canas = RetrofitClient.canaService.getAllCanaByUsuario(idUsuario)
                if (canas.isEmpty()) {
                    _state.value = HistorialCanaState.Empty
                } else {
                    _state.value = HistorialCanaState.Success(canas)
                }
            } catch (e: Exception) {
                _state.value = HistorialCanaState.Error("Error al cargar el historial: ${e.localizedMessage}")
            }
        }
    }
} 