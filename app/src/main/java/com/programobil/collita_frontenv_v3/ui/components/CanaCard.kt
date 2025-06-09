/* Aplicacion Collita v1
 Participantes: Godos García Jesús Emmanuel 217o02950,
                Ortiz Sánchez Néstor Éibar 217o03062,
                Axel David Ruiz Vargas 217o03139,
                Ramiro Aguilar Morales 207o02190*/

/**
 * CanaCard - Componente de tarjeta de caña
 * 
 * Descripción:
 * Componente que muestra la información de una caña
 * en un formato de tarjeta interactiva.
 * 
 * Características:
 * - Diseño de tarjeta con sombra
 * - Información detallada de la caña
 * - Imagen de la caña
 * - Estado de la caña
 * - Acciones según estado
 * - Animación al tocar
 * 
 * Información mostrada:
 * - Resumen de arañazos
 * - Cantidad total
 * - Hora de registro
 * - Imagen (si existe)
 * - Estado de pago
 * 
 * Parámetros:
 * - cana: CanaDto - Datos de la caña
 * - onEdit: () -> Unit - Acción al editar
 * - onDelete: () -> Unit - Acción al eliminar
 * - onViewDetails: () -> Unit - Acción al ver detalles
 * - modifier: Modifier - Modificadores de estilo y layout
 * 
 * Uso:
 * Se utiliza en:
 * - Lista de cañas
 * - Detalles de caña
 * - Historial de cañas
 * - Gestión de cañas
 * 
 * Ejemplo:
 * CanaCard(
 *     cana = canaDto,
 *     onEdit = { /* editar caña */ },
 *     onDelete = { /* eliminar caña */ },
 *     onViewDetails = { /* ver detalles */ },
 *     modifier = Modifier.padding(8.dp)
 * )
 */

package com.programobil.collita_frontenv_v3.ui.components 