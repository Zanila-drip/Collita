package com.programobil.collita_frontenv_v3.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.programobil.collita_frontenv_v3.data.api.UserResponse

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsuarioCardHistorialStyle(usuario: UserResponse) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { expanded = !expanded },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Nombre y botón editar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Usuario",
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "${usuario.nombreUsuario} ${usuario.apellidoPaternoUsuario} ${usuario.apellidoMaternoUsuario}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { /* TODO: Editar */ }) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Editar",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Divider(color = MaterialTheme.colorScheme.outlineVariant)
            // CURP
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = "CURP",
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "CURP: ${usuario.curpUsuario ?: "-"}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            // Teléfono
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Phone,
                    contentDescription = "Teléfono",
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Teléfono: ${usuario.telefono ?: "-"}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            // Correo
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Email,
                    contentDescription = "Correo",
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Correo: ${usuario.correo ?: "-"}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            // Acciones rápidas (solo si está expandido)
            if (expanded) {
                Divider(color = MaterialTheme.colorScheme.outlineVariant)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { /* TODO: Tickets */ },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.List,
                            contentDescription = "Tickets",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Tickets")
                    }
                    OutlinedButton(
                        onClick = { /* TODO: Pagar */ },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = "Pagar",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Pagar")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UsuarioCardHistorialStylePreview() {
    val usuario = UserResponse(
        nombreUsuario = "María",
        apellidoPaternoUsuario = "López",
        apellidoMaternoUsuario = "Martínez",
        correo = "maria@example.com",
        telefono = "0987654321",
        curpUsuario = "LMMM123456HDFABC02"
    )
    
    MaterialTheme {
        UsuarioCardHistorialStyle(usuario = usuario)
    }
} 