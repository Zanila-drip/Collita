package com.programobil.collita_frontenv_v3.data.api

import com.programobil.collita_frontenv_v3.data.model.LoginRequest
import com.programobil.collita_frontenv_v3.data.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("usuarios/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse
} 