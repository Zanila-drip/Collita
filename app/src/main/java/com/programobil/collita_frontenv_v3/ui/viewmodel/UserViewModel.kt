package com.programobil.collita_frontenv_v3.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.programobil.collita_frontenv_v3.data.api.RetrofitClient
import com.programobil.collita_frontenv_v3.data.api.UserResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val _state = MutableStateFlow<UserState>(UserState.Initial)
    val state: StateFlow<UserState> = _state

    private var currentUserId: String? = null

    fun setCurrentUserId(userId: String) {
        currentUserId = userId
        loadUser()
    }

    fun loadUser() {
        viewModelScope.launch {
            _state.value = UserState.Loading
            try {
                val userId = currentUserId ?: throw IllegalStateException("No hay usuario actual")
                val response = RetrofitClient.apiService.getUserById(userId)
                if (response != null) {
                    _state.value = UserState.Success(response)
                } else {
                    _state.value = UserState.Error("No se pudo cargar la información del usuario")
                }
            } catch (e: Exception) {
                _state.value = UserState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun logout() {
        currentUserId = null
        _state.value = UserState.LoggedOut
    }

    fun getCurrentUserId(): String? {
        return currentUserId
    }

    sealed class UserState {
        object Initial : UserState()
        object Loading : UserState()
        data class Success(val user: UserResponse) : UserState()
        data class Error(val message: String) : UserState()
        object LoggedOut : UserState()
    }
} 