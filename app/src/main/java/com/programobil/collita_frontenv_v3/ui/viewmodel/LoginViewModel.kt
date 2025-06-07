package com.programobil.collita_frontenv_v3.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.programobil.collita_frontenv_v3.data.api.ApiService
import com.programobil.collita_frontenv_v3.data.model.LoginRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class LoginViewModel(private val apiService: ApiService?) : ViewModel() {
    protected val _loginState = MutableStateFlow<LoginState>(LoginState.Initial)
    val loginState: StateFlow<LoginState> = _loginState

    open fun login(correo: String, curp: String) {
        if (apiService == null) return // Para previsualizaciones
        
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val response = apiService.login(LoginRequest(correo, curp))
                _loginState.value = LoginState.Validating
                delay(1000) // Simulamos un pequeño delay para mostrar el estado de validación
                _loginState.value = LoginState.Success(response.id)
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "Error desconocido")
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
    object Validating : LoginState()
    data class Success(val token: String) : LoginState()
    data class Error(val message: String) : LoginState()
} 