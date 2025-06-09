package com.programobil.collita_frontenv_v3.data.api

import java.time.LocalDate

data class PagoDto(
    val id: String? = null,
    val idUsuario: String,
    val fechaPago: String, // Usar String para compatibilidad con Retrofit
    val periodoInicio: String,
    val periodoFin: String,
    val monto: Double,
    val estado: String, // pendiente, pagado, rechazado
    val detalleCanaIds: List<String>? = null
) 