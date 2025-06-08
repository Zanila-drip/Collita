package com.programobil.collita_frontenv_v3.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.programobil.collita_frontenv_v3.ui.viewmodel.LoginViewModel
import com.programobil.collita_frontenv_v3.ui.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel,
    userViewModel: UserViewModel
) {
    var email by remember { mutableStateOf("leonardo.garcia@email.com") }
    var curp by remember { mutableStateOf("GARL900101HDFRRN01") }
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state) {
        if (state is LoginViewModel.LoginState.Success) {
            val response = (state as LoginViewModel.LoginState.Success).response
            userViewModel.setCurrentUserId(response.id)
            navController.navigate("dashboard") {
                popUpTo("login") { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    Column(
        modifier = Modifier
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
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = curp,
            onValueChange = { curp = it },
            label = { Text("CURP") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Button(
            onClick = { viewModel.login(email, curp) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = state !is LoginViewModel.LoginState.Loading
        ) {
            if (state is LoginViewModel.LoginState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Iniciar Sesión")
            }
        }

        if (state is LoginViewModel.LoginState.Error) {
            Text(
                text = (state as LoginViewModel.LoginState.Error).message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        TextButton(
            onClick = { navController.navigate("register") },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("¿No tienes cuenta? Regístrate")
        }
    }
} 