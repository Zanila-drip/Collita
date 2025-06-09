/* Aplicacion Collita v1 Participantes:  Godos García Jesús Emmanuel, Ortiz Sánchez Néstor Éibar, Peña Perez Axel, Axel David Ruiz Vargas, Ramiro Morales*/

package com.programobil.collita_frontenv_v3.data.api

import retrofit2.http.*

interface PagoApi {
    @GET("pagos")
    suspend fun getPagos(): List<PagoDto>

    @GET("pagos/usuario/{idUsuario}")
    suspend fun getPagosByUsuario(@Path("idUsuario") idUsuario: String): List<PagoDto>

    @POST("pagos")
    suspend fun crearPago(@Body pago: PagoDto): PagoDto

    @PUT("pagos/{id}")
    suspend fun actualizarPago(@Path("id") id: String, @Body pago: PagoDto): PagoDto
} 