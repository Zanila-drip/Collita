/* Aplicacion Collita v1
 Participantes: Godos García Jesús Emmanuel 217o02950,
                Ortiz Sánchez Néstor Éibar 217o03062,
                Peña Perez Axel  217o00677,
                Axel David Ruiz Vargas 217o03139,
                Ramiro Morales 207o02190*/

/**
 * DatePicker - Componente de selección de fecha
 * 
 * Descripción:
 * Componente que maneja la selección de fechas
 * con un diseño personalizado y validaciones.
 * 
 * Características:
 * - Selector de fecha nativo
 * - Formato de fecha personalizado
 * - Validación de fechas
 * - Límites de fecha configurables
 * - Diseño consistente con la app
 * 
 * Estados:
 * - Fecha no seleccionada
 * - Fecha seleccionada
 * - Fecha inválida
 * - Fecha fuera de rango
 * 
 * Parámetros:
 * - selectedDate: String - Fecha seleccionada
 * - onDateSelected: (String) -> Unit - Callback al seleccionar fecha
 * - minDate: String? - Fecha mínima permitida
 * - maxDate: String? - Fecha máxima permitida
 * - modifier: Modifier - Modificadores de estilo y layout
 * 
 * Uso:
 * Se utiliza en:
 * - Registro de cañas
 * - Programación de pagos
 * - Filtros de reportes
 * - Historial de operaciones
 * 
 * Ejemplo:
 * DatePicker(
 *     selectedDate = "2024-03-20",
 *     onDateSelected = { date -> /* procesar fecha */ },
 *     minDate = "2024-01-01",
 *     maxDate = "2024-12-31",
 *     modifier = Modifier.fillMaxWidth()
 * )
 */

package com.programobil.collita_frontenv_v3.ui.components 