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