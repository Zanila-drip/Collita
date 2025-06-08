package com.programobil.collita_frontenv_v3.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.programobil.collita_frontenv_v3.ui.viewmodel.RegisterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = viewModel()
) {
    var nombreUsuario by remember { mutableStateOf("Juan") }
    var apellidoPaterno by remember { mutableStateOf("Pérez") }
    var apellidoMaterno by remember { mutableStateOf("García") }
    var correo by remember { mutableStateOf("juan.perez@example.com") }
    var telefono by remember { mutableStateOf("5512345678") }
    var curpUsuario by remember { mutableStateOf("PEGJ123456HDFABC01") }

    val state by viewModel.state.collectAsState()

    LaunchedEffect(state) {
        if (state is RegisterViewModel.RegisterState.Success) {
            navController.navigate("login") {
                popUpTo("register") { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registro") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
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
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = curpUsuario,
                onValueChange = { curpUsuario = it },
                label = { Text("CURP") },
                modifier = Modifier.fillMaxWidth()
            )

            if (state is RegisterViewModel.RegisterState.Error) {
                Text(
                    text = (state as RegisterViewModel.RegisterState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Button(
                onClick = {
                    viewModel.register(
                        nombreUsuario = nombreUsuario,
                        apellidoPaterno = apellidoPaterno,
                        apellidoMaterno = apellidoMaterno,
                        correo = correo,
                        telefono = telefono,
                        curpUsuario = curpUsuario
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = state !is RegisterViewModel.RegisterState.Loading
            ) {
                if (state is RegisterViewModel.RegisterState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Registrarse")
                }
            }

            TextButton(
                onClick = { navController.navigateUp() }
            ) {
                Text("¿Ya tienes cuenta? Inicia sesión")
            }
        }
    }
} 