package com.esprit.jebi.ui.transaction

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esprit.jebi.data.local.entity.TransactionType
import com.esprit.jebi.data.local.entity.Wallet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    navController: NavController,
    viewModel: TransactionViewModel = hiltViewModel()
) {
    val wallets by viewModel.wallets.collectAsState()
    
    var amount by remember { mutableStateOf("") }
    var selectedWallet by remember { mutableStateOf<Wallet?>(null) }
    var selectedType by remember { mutableStateOf(TransactionType.EXPENSE) }
    var category by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    // Set default wallet if available and not selected
    LaunchedEffect(wallets) {
        if (selectedWallet == null && wallets.isNotEmpty()) {
            selectedWallet = wallets.first()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Add Transaction") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Amount Input
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // Wallet Selection
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedWallet?.name ?: "Select Wallet",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    wallets.forEach { wallet ->
                        DropdownMenuItem(
                            text = { Text(wallet.name) },
                            onClick = {
                                selectedWallet = wallet
                                expanded = false
                            }
                        )
                    }
                }
            }

            // Transaction Type Selection
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TransactionType.values().forEach { type ->
                    FilterChip(
                        selected = selectedType == type,
                        onClick = { selectedType = type },
                        label = { Text(type.name) }
                    )
                }
            }

            // Category Input
            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Category") },
                modifier = Modifier.fillMaxWidth()
            )

            // Note Input
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Note (Optional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (amount.isNotEmpty() && selectedWallet != null && category.isNotEmpty()) {
                        viewModel.addTransaction(
                            walletId = selectedWallet!!.id,
                            amount = amount.toDoubleOrNull() ?: 0.0,
                            type = selectedType,
                            category = category,
                            note = note
                        )
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = amount.isNotEmpty() && selectedWallet != null && category.isNotEmpty()
            ) {
                Text("Save Transaction")
            }
        }
    }
}
