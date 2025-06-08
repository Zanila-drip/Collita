package com.programobil.collita_frontenv_v3.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.programobil.collita_frontenv_v3.ui.viewmodel.RegisterViewModel
import com.programobil.collita_frontenv_v3.ui.viewmodel.RegisterViewModelFactory
import com.programobil.collita_frontenv_v3.data.api.RetrofitClient
import com.programobil.collita_frontenv_v3.ui.viewmodel.RegisterState

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    viewModel: RegisterViewModel = viewModel(factory = RegisterViewModelFactory(RetrofitClient.apiService))
) {
    var nombreUsuario by remember { mutableStateOf("María") }
    var apellidoPaterno by remember { mutableStateOf("Rodríguez") }
    var apellidoMaterno by remember { mutableStateOf("Sánchez") }
    var curp by remember { mutableStateOf("ROSM920315MDFRRN03") }
    var correo by remember { mutableStateOf("maria.rodriguez@email.com") }
    var telefono by remember { mutableStateOf("5599887766") }

    val registerState by viewModel.registerState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Registro de Usuario",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = nombreUsuario,
            onValueChange = { nombreUsuario = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = apellidoPaterno,
            onValueChange = { apellidoPaterno = it },
            label = { Text("Apellido Paterno") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = apellidoMaterno,
            onValueChange = { apellidoMaterno = it },
            label = { Text("Apellido Materno") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = curp,
            onValueChange = { curp = it },
            label = { Text("CURP") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo Electrónico") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = telefono,
            onValueChange = { telefono = it },
            label = { Text("Teléfono") },
            modifier = Modifier.fillMaxWidth()
        )

        when (registerState) {
            is RegisterState.Loading -> {
                CircularProgressIndicator()
            }
            is RegisterState.Error -> {
                Text(
                    text = (registerState as RegisterState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
            }
            is RegisterState.Success -> {
                LaunchedEffect(Unit) {
                    onRegisterSuccess()
                }
            }
            else -> {}
        }

        Button(
            onClick = {
                viewModel.register(
                    nombreUsuario = nombreUsuario,
                    apellidoPaterno = apellidoPaterno,
                    apellidoMaterno = apellidoMaterno,
                    curp = curp,
                    correo = correo,
                    telefono = telefono
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = registerState !is RegisterState.Loading
        ) {
            Text("Registrar")
        }
    }
} 