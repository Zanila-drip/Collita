package com.programobil.collita_frontenv_v3.data.api

data class CanaDto(
    val id: String? = null,
    val idUsuario: String,
    val horaInicioUsuario: String,
    val horaFinalUsuario: String,
    val cantidadCanaUsuario: Double,
    val fecha: String,
    val fechaUsuario: String,
    val resumenCosecha: String? = null
) 