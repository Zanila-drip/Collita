package com.programobil.collita_frontenv_v3.data.api

import retrofit2.http.GET
import retrofit2.http.Path

interface UsuarioService {
    @GET("usuarios/{id}")
    suspend fun getUsuarioById(@Path("id") id: String): UsuarioDto
} 