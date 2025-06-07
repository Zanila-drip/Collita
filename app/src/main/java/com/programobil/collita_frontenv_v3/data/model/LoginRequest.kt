package com.programobil.collita_frontenv_v3.data.model

data class LoginRequest(
    val correo: String,
    val curpUsuario: String
)

data class LoginResponse(
    val id: String,
    val nombreUsuario: String,
    val correo: String,
    val curpUsuario: String
) 