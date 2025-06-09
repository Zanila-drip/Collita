/* Aplicacion Collita v1 Participantes:  Godos García Jesús Emmanuel, Ortiz Sánchez Néstor Éibar, Peña Perez Axel, Axel David Ruiz Vargas, Ramiro Morales*/

package com.programobil.collita_frontenv_v3.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.programobil.collita_frontenv_v3.ui.viewmodel.LoginViewModel
import com.programobil.collita_frontenv_v3.ui.viewmodel.UserViewModel
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    var isAdminMode by remember { mutableStateOf(false) }

    // Credenciales precargadas
    val adminEmail = "Ramiro07@email.com"
    val adminCurp = "CURPADMIN123"
    val userEmail = "ana.lopez@example.com"
    val userCurp = "LOMA123456MDFABC02"

    var email by remember { mutableStateOf(userEmail) }
    var curp by remember { mutableStateOf(userCurp) }

    // Alternar entre usuario y admin
    fun switchUser() {
        if (isAdminMode) {
            email = userEmail
            curp = userCurp
        } else {
            email = adminEmail
            curp = adminCurp
        }
        isAdminMode = !isAdminMode
    }

    // Redirección tras login exitoso
    LaunchedEffect(state) {
        when (state) {
            is LoginViewModel.LoginState.Success -> {
                val response = (state as LoginViewModel.LoginState.Success).response
                val isAdmin = response.correo == adminEmail && response.curpUsuario == adminCurp
                userViewModel.setCurrentUserId(response.id)
                userViewModel.loadUser()
                if (isAdmin) {
                    navController.navigate("admin_nav") {
                        popUpTo("login") { inclusive = true }
                        launchSingleTop = true
                    }
                } else {
                    navController.navigate("dashboard") {
                        popUpTo("login") { inclusive = true }
                        launchSingleTop = true
                    }
                }
                // Resetea el estado para evitar bucles
                viewModel.setLoginState(LoginViewModel.LoginState.Initial)
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center
    ) {
        // Título grande y centrado
        Text(
            text = "Login",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 32.dp)
        )
        Button(
            onClick = { switchUser() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isAdminMode) "Usar cuenta de Ana" else "Usar cuenta de Admin")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { if (!isAdminMode) email = it },
            label = { Text("Correo electrónico") },
            enabled = !isAdminMode,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = curp,
            onValueChange = { if (!isAdminMode) curp = it },
            label = { Text("CURP") },
            enabled = !isAdminMode,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.login(email, curp) },
            modifier = Modifier.fillMaxWidth(),
            enabled = state !is LoginViewModel.LoginState.Loading
        ) {
            if (state is LoginViewModel.LoginState.Loading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                Text("Iniciar sesión")
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

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun LoginScreenPreview() {
    val navController = rememberNavController()
    MaterialTheme {
        LoginScreen(navController = navController)
    }
} 