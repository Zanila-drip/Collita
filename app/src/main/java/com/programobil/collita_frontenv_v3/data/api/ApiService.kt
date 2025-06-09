/* Aplicacion Collita v1
 Participantes: Godos García Jesús Emmanuel 217o02950,
                Ortiz Sánchez Néstor Éibar 217o03062,
                Peña Perez Axel  217o00677,
                Axel David Ruiz Vargas 217o03139,
                Ramiro Morales 207o02190*/

/**
 * ApiService - Interfaz principal para las llamadas a la API
 * 
 * Descripción:
 * Define los endpoints y métodos para interactuar con el backend.
 * Utiliza Retrofit para las llamadas HTTP y maneja la serialización
 * de datos con Gson.
 * 
 * Endpoints principales:
 * - Usuarios:
 *    * Login
 *    * Registro
 *    * Consulta
 *    * Actualización
 * - Cañas:
 *    * Registro
 *    * Consulta
 *    * Actualización
 *    * Eliminación
 * - Pagos:
 *    * Consulta
 *    * Actualización
 *    * Generación
 * 
 * Características:
 * - Manejo de autenticación
 * - Interceptores para tokens
 * - Conversión automática de JSON
 * - Manejo de errores
 * 
 * Uso:
 * Se instancia a través de RetrofitClient y se utiliza
 * en los ViewModels para realizar las operaciones
 * necesarias con el backend.
 */

package com.programobil.collita_frontenv_v3.data.api

import retrofit2.http.*

interface ApiService {
    @POST("usuarios/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @GET("usuarios/{id}")
    suspend fun getUserById(@Path("id") id: String): UserResponse?

    @POST("usuarios")
    suspend fun register(@Body user: UserResponse): UserResponse

    @GET("usuarios")
    suspend fun getAllUsuarios(): List<UserResponse>

    @DELETE("usuarios/{id}")
    suspend fun deleteUsuario(@Path("id") id: String): Unit?
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