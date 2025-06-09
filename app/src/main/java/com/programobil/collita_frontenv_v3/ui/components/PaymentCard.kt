/* Aplicacion Collita v1
 Participantes: Godos García Jesús Emmanuel 217o02950,
                Ortiz Sánchez Néstor Éibar 217o03062,
                Axel David Ruiz Vargas 217o03139,
                Ramiro Aguilar Morales 207o02190*/

/**
 * PaymentCard - Componente de tarjeta de pago
 * 
 * Descripción:
 * Componente que muestra la información de un pago
 * en un formato de tarjeta interactiva.
 * 
 * Características:
 * - Diseño de tarjeta con sombra
 * - Información detallada del pago
 * - Estado de pago
 * - Acciones según estado
 * - Animación al tocar
 * 
 * Información mostrada:
 * - Monto total
 * - Fecha de cobro
 * - Estado del pago
 * - Cañas asociadas
 * - Usuario
 * 
 * Parámetros:
 * - payment: PagoDto - Datos del pago
 * - onEdit: () -> Unit - Acción al editar
 * - onDelete: () -> Unit - Acción al eliminar
 * - onViewDetails: () -> Unit - Acción al ver detalles
 * - modifier: Modifier - Modificadores de estilo y layout
 * 
 * Uso:
 * Se utiliza en:
 * - Lista de pagos
 * - Detalles de pago
 * - Historial de pagos
 * - Gestión de pagos
 * 
 * Ejemplo:
 * PaymentCard(
 *     payment = pagoDto,
 *     onEdit = { /* editar pago */ },
 *     onDelete = { /* eliminar pago */ },
 *     onViewDetails = { /* ver detalles */ },
 *     modifier = Modifier.padding(8.dp)
 * )
 */

package com.programobil.collita_frontenv_v3.ui.components 