package com.programobil.collita_frontenv_v3.data.api

import retrofit2.http.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

interface CanaApi {
    @POST("cana")
    suspend fun createCana(@Body canaDto: CanaDto): CanaDto

    @GET("cana/usuario/{idUsuario}")
    suspend fun getCanaByUsuario(
        @Path("idUsuario") idUsuario: String,
        @Query("fecha") fecha: String
    ): List<CanaDto>

    @GET("cana/usuario/{idUsuario}/todas")
    suspend fun getAllCanaByUsuario(@Path("idUsuario") idUsuario: String): List<CanaDto>

    @GET("cana/fecha/{fecha}")
    suspend fun getCanaByFecha(@Path("fecha") fecha: String): List<CanaDto>

    @PUT("cana/{id}")
    suspend fun updateCana(@Path("id") id: String, @Body canaDto: CanaDto): CanaDto

    @Multipart
    @POST("cana/con-imagen")
    suspend fun createCanaConImagen(
        @Part file: MultipartBody.Part,
        @Part("idUsuario") idUsuario: RequestBody,
        @Part("horaInicioUsuario") horaInicioUsuario: RequestBody,
        @Part("horaFinalUsuario") horaFinalUsuario: RequestBody?,
        @Part("cantidadCanaUsuario") cantidadCanaUsuario: RequestBody,
        @Part("fecha") fecha: RequestBody,
        @Part("fechaUsuario") fechaUsuario: RequestBody,
        @Part("resumenCosecha") resumenCosecha: RequestBody
    ): Response<CanaDto>

    @DELETE("cana/usuario/{idUsuario}")
    suspend fun deleteCanasByUsuario(@Path("idUsuario") idUsuario: String): Unit?

    @GET("cana/{id}")
    suspend fun getCanaById(@Path("id") id: String): CanaDto
} 