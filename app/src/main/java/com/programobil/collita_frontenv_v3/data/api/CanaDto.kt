/* Aplicacion Collita v1
 Participantes: Godos García Jesús Emmanuel 217o02950,
                Ortiz Sánchez Néstor Éibar 217o03062,
                Axel David Ruiz Vargas 217o03139,
                Ramiro Aguilar Morales 207o02190*/
package com.programobil.collita_frontenv_v3.data.api

data class CanaDto(
    val id: String? = null,
    val idUsuario: String,
    val horaInicioUsuario: String,
    val horaFinalUsuario: String,
    val cantidadCanaUsuario: Double,
    val fecha: String,
    val fechaUsuario: String,
    val resumenCosecha: String? = null,
    val imagenPath: String? = null
) 