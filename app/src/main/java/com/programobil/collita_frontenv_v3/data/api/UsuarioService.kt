/* Aplicacion Collita v1
 Participantes: Godos García Jesús Emmanuel 217o02950,
                Ortiz Sánchez Néstor Éibar 217o03062,
                Axel David Ruiz Vargas 217o03139,
                Ramiro Aguilar Morales 207o02190*/
/**
 * UsuarioService - Servicio para operaciones de usuarios
 * 
 * Descripción:
 * Define los endpoints específicos para la gestión de usuarios.
 * Maneja todas las operaciones CRUD relacionadas con usuarios.
 * 
 * Endpoints:
 * - GET /usuarios
 *    * Obtiene lista de todos los usuarios
 *    * Retorna: List<UsuarioDto>
 * 
 * - GET /usuarios/{id}
 *    * Obtiene un usuario específico
 *    * Parámetros: id (String)
 *    * Retorna: UsuarioDto
 * 
 * - POST /usuarios/login
 *    * Autentica un usuario
 *    * Parámetros: LoginRequestDto
 *    * Retorna: UsuarioDto
 * 
 * Características:
 * - Validación de datos
 * - Manejo de errores HTTP
 * - Serialización automática
 * 
 * Uso:
 * Se utiliza principalmente en:
 * - LoginViewModel
 * - RegisterViewModel
 * - AdminViewModel
 */

package com.programobil.collita_frontenv_v3.data.api

import retrofit2.http.GET
import retrofit2.http.Path

interface UsuarioService {
    @GET("usuarios")
    suspend fun getAll(): List<UsuarioDto>

    @GET("usuarios/{id}")
    suspend fun getUsuarioById(@Path("id") id: String): UsuarioDto
} 