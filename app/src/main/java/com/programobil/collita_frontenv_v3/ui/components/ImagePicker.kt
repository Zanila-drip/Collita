/* Aplicacion Collita v1
 Participantes: Godos García Jesús Emmanuel 217o02950,
                Ortiz Sánchez Néstor Éibar 217o03062,
                Axel David Ruiz Vargas 217o03139,
                Ramiro Aguilar Morales 207o02190*/

/**
 * ImagePicker - Componente de selección de imágenes
 * 
 * Descripción:
 * Componente que maneja la selección y visualización
 * de imágenes desde la galería o cámara.
 * 
 * Características:
 * - Selección desde galería
 * - Captura desde cámara
 * - Vista previa de imagen
 * - Compresión automática
 * - Manejo de permisos
 * 
 * Estados:
 * - Sin imagen seleccionada
 * - Imagen seleccionada
 * - Cargando imagen
 * - Error de permisos
 * 
 * Parámetros:
 * - onImageSelected: (Uri) -> Unit - Callback al seleccionar imagen
 * - modifier: Modifier - Modificadores de estilo y layout
 * 
 * Uso:
 * Se utiliza en:
 * - Registro de cañas
 * - Actualización de perfil
 * - Documentación de pagos
 * 
 * Ejemplo:
 * ImagePicker(
 *     onImageSelected = { uri -> /* procesar imagen */ },
 *     modifier = Modifier.fillMaxWidth()
 * )
 */

package com.programobil.collita_frontenv_v3.ui.components 