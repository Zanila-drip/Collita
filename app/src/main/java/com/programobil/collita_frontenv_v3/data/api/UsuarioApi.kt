package com.programobil.collita_frontenv_v3.data.api

import retrofit2.http.DELETE
import retrofit2.http.Path

interface UsuarioApi {
    @DELETE("usuarios/{id}")
    suspend fun deleteUsuario(@Path("id") id: String): Unit? {
        return null
    }
} 