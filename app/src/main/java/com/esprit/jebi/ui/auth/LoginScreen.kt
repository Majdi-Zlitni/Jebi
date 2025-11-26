package com.esprit.jebi.ui.auth

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.VisualTransformation
import androidx.fragment.app.FragmentActivity

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onNavigateToDashboard: () -> Unit,
    onNavigateToVerification: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsState()
    
    val context = LocalContext.current
    
    val biometricPromptManager = remember {
        BiometricPromptManager(context as FragmentActivity)
    }
    
    LaunchedEffect(biometricPromptManager) {
        biometricPromptManager.promptResults.collect { result ->
            if (result is BiometricPromptManager.BiometricResult.AuthenticationSuccess) {
                viewModel.loginWithBiometrics()
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
        Text(text = "Login", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else
                    Icons.Filled.VisibilityOff

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = if (passwordVisible) "Hide password" else "Show password")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = { viewModel.login(email, password) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedButton(
            onClick = {
                if (viewModel.hasStoredCredentials()) {
                    biometricPromptManager.showBiometricPrompt(
                        title = "Login with Biometrics",
                        description = "Use your fingerprint or face to login"
                    )
                }
            },
            enabled = viewModel.hasStoredCredentials(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Fingerprint, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Login with Biometrics")
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        TextButton(onClick = onNavigateToRegister) {
            Text("Don't have an account? Register")
        }

        if (uiState.isLoading) {
            CircularProgressIndicator()
        }
        
        if (uiState.error != null) {
            Text(text = uiState.error!!, color = MaterialTheme.colorScheme.error)
        }
        
        LaunchedEffect(uiState.isAuthenticated) {
            if (uiState.isAuthenticated) {
                onNavigateToDashboard()
            }
        }
        
        LaunchedEffect(uiState.needsVerification) {
            if (uiState.needsVerification) {
                onNavigateToVerification()
            }
        }
    }
}
