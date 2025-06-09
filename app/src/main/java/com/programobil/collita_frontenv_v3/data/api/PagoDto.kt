/* Aplicacion Collita v1
 Participantes: Godos García Jesús Emmanuel 217o02950,
                Ortiz Sánchez Néstor Éibar 217o03062,
                Axel David Ruiz Vargas 217o03139,
                Ramiro Aguilar Morales 207o02190*/
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