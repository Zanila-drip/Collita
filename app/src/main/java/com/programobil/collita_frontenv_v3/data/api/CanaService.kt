package com.programobil.collita_frontenv_v3.data.api

import retrofit2.http.*

interface CanaService {
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
} 