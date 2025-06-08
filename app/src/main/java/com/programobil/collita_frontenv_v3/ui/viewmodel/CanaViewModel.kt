package com.programobil.collita_frontenv_v3.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.programobil.collita_frontenv_v3.data.api.CanaDto
import com.programobil.collita_frontenv_v3.data.api.CanaService
import com.programobil.collita_frontenv_v3.data.api.RetrofitClient
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
    private val canaService: CanaService = RetrofitClient.canaService

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