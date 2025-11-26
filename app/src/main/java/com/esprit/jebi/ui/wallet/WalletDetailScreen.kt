package com.esprit.jebi.ui.wallet

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.esprit.jebi.data.local.entity.Wallet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletDetailScreen(
    walletId: Int,
    onNavigateBack: () -> Unit,
    viewModel: WalletViewModel = hiltViewModel()
) {
    LaunchedEffect(walletId) {
        viewModel.loadWallet(walletId)
    }

    val wallet by viewModel.selectedWallet.collectAsState()
    var isEditing by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Edit states
    var name by remember { mutableStateOf("") }
    var currency by remember { mutableStateOf("") }
    var balance by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }

    // Initialize edit states when wallet loads or editing starts
    LaunchedEffect(wallet, isEditing) {
        wallet?.let {
            if (!isEditing) {
                name = it.name
                currency = it.currency
                balance = it.balance.toString()
                type = it.type
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Edit Wallet" else "Wallet Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (wallet != null) {
                        if (isEditing) {
                            IconButton(onClick = {
                                val newBalance = balance.toDoubleOrNull() ?: 0.0
                                viewModel.updateWallet(wallet!!.copy(
                                    name = name,
                                    currency = currency,
                                    balance = newBalance,
                                    type = type
                                ))
                                isEditing = false
                            }) {
                                Icon(Icons.Default.Save, contentDescription = "Save")
                            }
                        } else {
                            IconButton(onClick = { isEditing = true }) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit")
                            }
                            IconButton(onClick = { showDeleteDialog = true }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete")
                            }
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        if (wallet == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (isEditing) {
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
                        label = { Text("Balance") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = type,
                        onValueChange = { type = it },
                        label = { Text("Type") },
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    DetailItem(label = "Name", value = wallet!!.name)
                    DetailItem(label = "Currency", value = wallet!!.currency)
                    DetailItem(label = "Balance", value = "${wallet!!.currency} ${wallet!!.balance}")
                    DetailItem(label = "Type", value = wallet!!.type)
                }
            }
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Delete Wallet") },
                text = { Text("Are you sure you want to delete this wallet?") },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.deleteWallet(wallet!!)
                        showDeleteDialog = false
                        onNavigateBack()
                    }) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun DetailItem(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
        Divider(modifier = Modifier.padding(vertical = 8.dp))
    }
}
