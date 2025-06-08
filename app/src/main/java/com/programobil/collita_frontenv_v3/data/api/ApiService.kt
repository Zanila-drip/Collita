package com.programobil.collita_frontenv_v3.data.api

import retrofit2.http.*

interface ApiService {
    @POST("usuarios/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @GET("usuarios/{id}")
    suspend fun getUserById(@Path("id") id: String): UserResponse?

    @POST("usuarios")
    suspend fun register(@Body user: UserResponse): UserResponse
}

data class LoginRequest(
    val correo: String,
    val curpUsuario: String
)

data class LoginResponse(
    val id: String,
    val nombreUsuario: String?,
    val apellidoPaternoUsuario: String?,
    val apellidoMaternoUsuario: String?,
    val correo: String?,
    val telefono: String?,
    val curpUsuario: String?
)

data class UserResponse(
    val id: String? = null,
    val nombreUsuario: String? = null,
    val apellidoPaternoUsuario: String? = null,
    val apellidoMaternoUsuario: String? = null,
    val correo: String? = null,
    val telefono: String? = null,
    val curpUsuario: String? = null
) 