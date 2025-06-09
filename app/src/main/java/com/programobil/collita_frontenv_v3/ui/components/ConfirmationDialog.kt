/* Aplicacion Collita v1
 Participantes: Godos García Jesús Emmanuel 217o02950,
                Ortiz Sánchez Néstor Éibar 217o03062,
                Peña Perez Axel  217o00677,
                Axel David Ruiz Vargas 217o03139,
                Ramiro Morales 207o02190*/

/**
 * ConfirmationDialog - Componente de diálogo de confirmación
 * 
 * Descripción:
 * Componente que muestra un diálogo de confirmación
 * para acciones importantes o destructivas.
 * 
 * Características:
 * - Diseño de diálogo nativo
 * - Título personalizable
 * - Mensaje detallado
 * - Botones de confirmar/cancelar
 * - Animación de entrada/salida
 * 
 * Estados:
 * - Diálogo visible
 * - Diálogo oculto
 * - Procesando acción
 * 
 * Parámetros:
 * - title: String - Título del diálogo
 * - message: String - Mensaje de confirmación
 * - onConfirm: () -> Unit - Acción al confirmar
 * - onDismiss: () -> Unit - Acción al cancelar
 * - confirmText: String - Texto del botón confirmar
 * - dismissText: String - Texto del botón cancelar
 * 
 * Uso:
 * Se utiliza para:
 * - Eliminar cañas
 * - Cancelar pagos
 * - Cerrar sesión
 * - Operaciones críticas
 * 
 * Ejemplo:
 * ConfirmationDialog(
 *     title = "Eliminar caña",
 *     message = "¿Estás seguro de eliminar esta caña?",
 *     onConfirm = { /* eliminar caña */ },
 *     onDismiss = { /* cerrar diálogo */ },
 *     confirmText = "Eliminar",
 *     dismissText = "Cancelar"
 * )
 */

package com.programobil.collita_frontenv_v3.ui.components 