package com.programobil.collita_frontenv_v3.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.programobil.collita_frontenv_v3.data.api.ApiService
import com.programobil.collita_frontenv_v3.data.api.LoginRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val apiService: ApiService?) : ViewModel() {
    private val TAG = "LoginViewModel"
    
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Initial)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    fun login(correo: String, curp: String) {
        Log.d(TAG, "Intentando login con correo: $correo y curp: $curp")
        
        if (apiService == null) {
            Log.e(TAG, "ApiService es null")
            _loginState.value = LoginState.Error("Error de configuración: ApiService no disponible")
            return
        }

        if (correo.isBlank() || curp.isBlank()) {
            _loginState.value = LoginState.Error("Por favor complete todos los campos")
            return
        }

        _loginState.value = LoginState.Loading
        
        viewModelScope.launch {
            try {
                Log.d(TAG, "Enviando petición de login")
                val request = LoginRequest(correo = correo, curpUsuario = curp)
                val response = apiService.login(request)
                Log.d(TAG, "Respuesta recibida: $response")
                _loginState.value = LoginState.Success(response.nombreUsuario)
            } catch (e: Exception) {
                Log.e(TAG, "Error en login: ${e.message}", e)
                _loginState.value = LoginState.Error("Error al iniciar sesión: ${e.message}")
            }
        }
    }

    // Método para previsualizaciones
    fun setLoginState(state: LoginState) {
        _loginState.value = state
    }
}

sealed class LoginState {
    object Initial : LoginState()
    object Loading : LoginState()
    data class Success(val userName: String) : LoginState()
    data class Error(val message: String) : LoginState()
} 