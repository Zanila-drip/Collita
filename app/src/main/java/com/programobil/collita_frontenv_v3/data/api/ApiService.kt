package com.programobil.collita_frontenv_v3.data.api

import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("usuarios/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("usuarios")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse
}

data class LoginRequest(
    val correo: String,
    val curpUsuario: String
)

data class LoginResponse(
    val id: String,
    val nombreUsuario: String,
    val correo: String,
    val curpUsuario: String
)

data class RegisterRequest(
    val nombreUsuario: String,
    val apellidoPaternoUsuario: String,
    val apellidoMaternoUsuario: String,
    val curpUsuario: String,
    val correo: String,
    val telefono: String
)

data class RegisterResponse(
    val id: String,
    val nombreUsuario: String,
    val correo: String,
    val curpUsuario: String
) 