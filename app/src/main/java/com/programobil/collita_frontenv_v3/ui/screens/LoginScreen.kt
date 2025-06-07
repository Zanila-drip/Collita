package com.programobil.collita_frontenv_v3.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.programobil.collita_frontenv_v3.ui.theme.CollitaFrontenvv3Theme
import com.programobil.collita_frontenv_v3.ui.viewmodel.LoginState
import com.programobil.collita_frontenv_v3.ui.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    modifier: Modifier = Modifier
) {
    var correo by remember { mutableStateOf("") }
    var curp by remember { mutableStateOf("") }
    val loginState by viewModel.loginState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Iniciar Sesión",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo Electrónico") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            enabled = loginState !is LoginState.Loading && loginState !is LoginState.Validating
        )

        OutlinedTextField(
            value = curp,
            onValueChange = { curp = it },
            label = { Text("CURP") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            enabled = loginState !is LoginState.Loading && loginState !is LoginState.Validating
        )

        Button(
            onClick = { viewModel.login(correo, curp) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = loginState !is LoginState.Loading && loginState !is LoginState.Validating
        ) {
            when (loginState) {
                is LoginState.Loading -> {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                is LoginState.Validating -> {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Text("Validando usuario...")
                    }
                }
                else -> {
                    Text("Iniciar Sesión")
                }
            }
        }

        when (loginState) {
            is LoginState.Error -> {
                Text(
                    text = (loginState as LoginState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            is LoginState.Validating -> {
                Text(
                    text = "Usuario validado ✓",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            else -> {}
        }
    }
}

// Clase de prueba para previsualizaciones
private class PreviewLoginViewModel : LoginViewModel(null) {
    fun setPreviewState(state: LoginState) {
        _loginState.value = state
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    CollitaFrontenvv3Theme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val previewViewModel = PreviewLoginViewModel()
            LoginScreen(viewModel = previewViewModel)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenLoadingPreview() {
    CollitaFrontenvv3Theme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val previewViewModel = PreviewLoginViewModel()
            previewViewModel.setPreviewState(LoginState.Loading)
            LoginScreen(viewModel = previewViewModel)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenValidatingPreview() {
    CollitaFrontenvv3Theme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val previewViewModel = PreviewLoginViewModel()
            previewViewModel.setPreviewState(LoginState.Validating)
            LoginScreen(viewModel = previewViewModel)
        }
    }
} 