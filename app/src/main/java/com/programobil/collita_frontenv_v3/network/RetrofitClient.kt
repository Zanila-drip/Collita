package com.programobil.collita_frontenv_v3.network

import com.programobil.collita_frontenv_v3.data.api.ApiService
import com.programobil.collita_frontenv_v3.data.api.CanaApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.programobil.collita_frontenv_v3.data.api.UsuarioService
import com.programobil.collita_frontenv_v3.data.api.PagoApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object RetrofitClient {
    // Para emulador: 10.0.2.2
    // Para dispositivo físico:
    private const val BASE_URL = "http://64.23.243.112:8080/api/v1/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
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

    val pagoService: PagoApi by lazy {
        retrofit.create(PagoApi::class.java)
    }
} 