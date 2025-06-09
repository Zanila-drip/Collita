/* Aplicacion Collita v1
 Participantes: Godos García Jesús Emmanuel 217o02950,
                Ortiz Sánchez Néstor Éibar 217o03062,
                Axel David Ruiz Vargas 217o03139,
                Ramiro Aguilar Morales 207o02190*/

/**
 * UserCard - Componente de tarjeta de usuario
 * 
 * Descripción:
 * Componente que muestra la información de un usuario
 * en un formato de tarjeta interactiva.
 * 
 * Características:
 * - Diseño de tarjeta con sombra
 * - Información básica del usuario
 * - Estado de cuenta
 * - Acciones rápidas
 * - Animación al tocar
 * 
 * Información mostrada:
 * - Nombre completo
 * - Email
 * - Teléfono
 * - Estado de cuenta
 * - Fecha de registro
 * 
 * Parámetros:
 * - user: UsuarioDto - Datos del usuario
 * - onEdit: () -> Unit - Acción al editar
 * - onDelete: () -> Unit - Acción al eliminar
 * - modifier: Modifier - Modificadores de estilo y layout
 * 
 * Uso:
 * Se utiliza en:
 * - Lista de usuarios
 * - Detalles de usuario
 * - Búsqueda de usuarios
 * - Gestión de usuarios
 * 
 * Ejemplo:
 * UserCard(
 *     user = usuarioDto,
 *     onEdit = { /* editar usuario */ },
 *     onDelete = { /* eliminar usuario */ },
 *     modifier = Modifier.padding(8.dp)
 * )
 */

package com.programobil.collita_frontenv_v3.ui.components 