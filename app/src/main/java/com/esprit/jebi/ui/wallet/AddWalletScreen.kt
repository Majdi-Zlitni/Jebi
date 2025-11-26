package com.esprit.jebi.ui.wallet

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWalletScreen(
    onNavigateBack: () -> Unit,
    viewModel: WalletViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var currency by remember { mutableStateOf("USD") }
    var balance by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("Cash") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Wallet") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Wallet Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = currency,
                onValueChange = { currency = it },
                label = { Text("Currency") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = balance,
                onValueChange = { balance = it },
                label = { Text("Initial Balance") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = type,
                onValueChange = { type = it },
                label = { Text("Type (e.g., Cash, Bank)") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    val balanceValue = balance.toDoubleOrNull() ?: 0.0
                    viewModel.addWallet(name, currency, balanceValue, type)
                    onNavigateBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Wallet")
            }
        }
    }
}
