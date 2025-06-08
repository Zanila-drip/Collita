package com.programobil.collita_frontenv_v3.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.programobil.collita_frontenv_v3.data.api.CanaService
import com.programobil.collita_frontenv_v3.data.api.UsuarioService
import com.programobil.collita_frontenv_v3.data.api.ApiService

object RetrofitClient {
//    private const val BASE_URL = "http://165.232.146.36:8080/api/v1/" // IP del backend en la red local
    private const val BASE_URL = "http://192.168.1.71:8080/api/v1/" // IP del backend en la red local

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val canaService: CanaService = retrofit.create(CanaService::class.java)
    val usuarioService: UsuarioService = retrofit.create(UsuarioService::class.java)
    val apiService: ApiService = retrofit.create(ApiService::class.java)
} 