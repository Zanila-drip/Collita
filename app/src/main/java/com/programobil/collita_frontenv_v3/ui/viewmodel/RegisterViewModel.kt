package com.programobil.collita_frontenv_v3.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.programobil.collita_frontenv_v3.data.api.RetrofitClient
import com.programobil.collita_frontenv_v3.data.api.UserResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val _state = MutableStateFlow<RegisterState>(RegisterState.Initial)
    val state: StateFlow<RegisterState> = _state.asStateFlow()

    fun register(
        nombreUsuario: String,
        apellidoPaterno: String,
        apellidoMaterno: String,
        correo: String,
        telefono: String,
        curpUsuario: String
    ) {
        viewModelScope.launch {
            _state.value = RegisterState.Loading
            try {
                if (nombreUsuario.isBlank() || apellidoPaterno.isBlank() || 
                    apellidoMaterno.isBlank() || correo.isBlank() || 
                    telefono.isBlank() || curpUsuario.isBlank()) {
                    _state.value = RegisterState.Error("Todos los campos son obligatorios")
                    return@launch
                }

                val user = UserResponse(
                    nombreUsuario = nombreUsuario,
                    apellidoPaternoUsuario = apellidoPaterno,
                    apellidoMaternoUsuario = apellidoMaterno,
                    correo = correo,
                    telefono = telefono,
                    curpUsuario = curpUsuario
                )

                val response = RetrofitClient.apiService.register(user)
                _state.value = RegisterState.Success(response)
            } catch (e: Exception) {
                _state.value = RegisterState.Error("Error al registrar: ${e.message}")
            }
        }
    }

    sealed class RegisterState {
        object Initial : RegisterState()
        object Loading : RegisterState()
        data class Success(val user: UserResponse) : RegisterState()
        data class Error(val message: String) : RegisterState()
    }
} 