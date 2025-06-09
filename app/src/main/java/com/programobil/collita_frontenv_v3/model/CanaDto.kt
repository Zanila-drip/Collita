/* Aplicacion Collita v1
 Participantes: Godos García Jesús Emmanuel 217o02950,
                Ortiz Sánchez Néstor Éibar 217o03062,
                Axel David Ruiz Vargas 217o03139,
                Ramiro Aguilar Morales 207o02190*/
package com.programobil.collita_frontenv_v3.model

data class CanaDto(
    val id: String,
    val idUsuario: String,
    val cantidadCanaUsuario: Int,
    val horaInicioUsuario: String,
    val horaFinalUsuario: String,
    val fecha: String
) 