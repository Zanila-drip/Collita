package com.programobil.collita_frontenv_v3.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.programobil.collita_frontenv_v3.data.api.ApiService
import com.programobil.collita_frontenv_v3.data.api.RegisterRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed class RegisterState {
    object Initial : RegisterState()
    object Loading : RegisterState()
    data class Success(val message: String) : RegisterState()
    data class Error(val message: String) : RegisterState()
}

class RegisterViewModel(private val apiService: ApiService?) : ViewModel() {
    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Initial)
    val registerState: StateFlow<RegisterState> = _registerState

    fun register(
        nombreUsuario: String,
        apellidoPaterno: String,
        apellidoMaterno: String,
        curp: String,
        correo: String,
        telefono: String
    ) {
        if (apiService == null) {
            _registerState.value = RegisterState.Error("Error de configuración: ApiService no disponible")
            return
        }

        _registerState.value = RegisterState.Loading

        viewModelScope.launch {
            try {
                val request = RegisterRequest(
                    nombreUsuario = nombreUsuario,
                    apellidoPaternoUsuario = apellidoPaterno,
                    apellidoMaternoUsuario = apellidoMaterno,
                    curpUsuario = curp,
                    correo = correo,
                    telefono = telefono
                )

                val response = apiService.register(request)
                _registerState.value = RegisterState.Success("Usuario registrado exitosamente")
            } catch (e: HttpException) {
                when (e.code()) {
                    409 -> _registerState.value = RegisterState.Error("Ya existe un usuario con este correo o CURP")
                    400 -> _registerState.value = RegisterState.Error("Datos inválidos. Por favor verifica la información")
                    else -> _registerState.value = RegisterState.Error("Error del servidor: ${e.message()}")
                }
            } catch (e: IOException) {
                _registerState.value = RegisterState.Error("Error de conexión. Verifica tu conexión a internet")
            } catch (e: Exception) {
                _registerState.value = RegisterState.Error("Error al registrar usuario: ${e.message}")
            }
        }
    }
} 