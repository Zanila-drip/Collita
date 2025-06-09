/* Aplicacion Collita v1
 Participantes: Godos García Jesús Emmanuel 217o02950,
                Ortiz Sánchez Néstor Éibar 217o03062,
                Peña Perez Axel  217o00677,
                Axel David Ruiz Vargas 217o03139,
                Ramiro Morales 207o02190*/

/**
 * LoginViewModel - ViewModel para la autenticación de usuarios
 * 
 * Responsabilidades:
 * 1. Gestión de autenticación:
 *    - Validación de credenciales
 *    - Manejo de sesión
 *    - Redirección según rol
 * 
 * Estados manejados:
 * - Autenticación:
 *    * Estado de login
 *    * Usuario actual
 *    * Token de sesión
 *    * Errores de autenticación
 * - Formulario:
 *    * Correo
 *    * CURP
 *    * Estado de validación
 * 
 * Funciones principales:
 * - Validar credenciales
 * - Iniciar sesión
 * - Cerrar sesión
 * - Verificar sesión activa
 * - Redirigir según rol
 * 
 * Eventos:
 * - Login exitoso
 * - Error de autenticación
 * - Sesión expirada
 * - Cambio de estado
 */

package com.programobil.collita_frontenv_v3.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.programobil.collita_frontenv_v3.network.RetrofitClient
import com.programobil.collita_frontenv_v3.data.api.LoginRequest
import com.programobil.collita_frontenv_v3.data.api.LoginResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val TAG = "LoginViewModel"
    
    private val _state = MutableStateFlow<LoginState>(LoginState.Initial)
    val state: StateFlow<LoginState> = _state

    fun login(correo: String, curpUsuario: String) {
        Log.d(TAG, "Intentando login con correo: $correo y CURP: $curpUsuario")
        
        if (correo.isBlank() || curpUsuario.isBlank()) {
            _state.value = LoginState.Error("Por favor complete todos los campos")
            return
        }

        _state.value = LoginState.Loading
        
        viewModelScope.launch {
            try {
                Log.d(TAG, "Enviando petición de login")
                val request = LoginRequest(correo = correo, curpUsuario = curpUsuario)
                val response = com.programobil.collita_frontenv_v3.network.RetrofitClient.apiService.login(request)
                Log.d(TAG, "Respuesta recibida: $response")
                _state.value = LoginState.Success(response)
            } catch (e: Exception) {
                Log.e(TAG, "Error en login: ${e.message}", e)
                _state.value = LoginState.Error("Error al iniciar sesión: ${e.message}")
            }
        }
    }

    // Método para previsualizaciones
    fun setLoginState(state: LoginState) {
        _state.value = state
    }

    sealed class LoginState {
        object Initial : LoginState()
        object Loading : LoginState()
        data class Success(val response: LoginResponse) : LoginState()
        data class Error(val message: String) : LoginState()
    }
}

class LoginViewModelFactory(
    private val apiService: com.programobil.collita_frontenv_v3.data.api.ApiService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 