package com.programobil.collita_frontenv_v3.network

import com.programobil.collita_frontenv_v3.data.api.ApiService
import com.programobil.collita_frontenv_v3.data.api.CanaApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.programobil.collita_frontenv_v3.data.api.UsuarioService

object RetrofitClient {
    private const val BASE_URL = "http://64.23.166.183:8080/api/v1/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    val canaService: CanaApi by lazy {
        retrofit.create(CanaApi::class.java)
    }

    val usuarioService: UsuarioService = retrofit.create(UsuarioService::class.java)
} 