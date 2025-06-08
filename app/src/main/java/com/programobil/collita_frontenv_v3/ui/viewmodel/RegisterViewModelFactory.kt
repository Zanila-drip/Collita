package com.programobil.collita_frontenv_v3.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.programobil.collita_frontenv_v3.data.api.ApiService

class RegisterViewModelFactory(private val apiService: ApiService?) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 