package com.programobil.collita_frontenv_v3.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.programobil.collita_frontenv_v3.ui.viewmodel.LoginViewModel
import com.programobil.collita_frontenv_v3.ui.viewmodel.LoginViewModelFactory
import com.programobil.collita_frontenv_v3.data.api.RetrofitClient
import com.programobil.collita_frontenv_v3.ui.viewmodel.LoginState

@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit,
    onRegisterClick: () -> Unit,
    viewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(RetrofitClient.apiService))
) {
    var correo by remember { mutableStateOf("maria.rodriguez@email.com") }
    var curp by remember { mutableStateOf("ROSM920315MDFRRN03") }

    val loginState by viewModel.loginState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Iniciar Sesión",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo Electrónico") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = curp,
            onValueChange = { curp = it },
            label = { Text("CURP") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        when (loginState) {
            is LoginState.Loading -> {
                CircularProgressIndicator()
            }
            is LoginState.Error -> {
                Text(
                    text = (loginState as LoginState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
            }
            is LoginState.Success -> {
                LaunchedEffect(Unit) {
                    onLoginSuccess((loginState as LoginState.Success).userName)
                }
            }
            else -> {}
        }

        Button(
            onClick = {
                viewModel.login(correo, curp)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = loginState !is LoginState.Loading
        ) {
            Text("Iniciar Sesión")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = onRegisterClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("¿No tienes cuenta? Regístrate aquí")
        }
    }
} 