package com.esprit.jebi.ui.wallet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.esprit.jebi.data.local.entity.Wallet
import com.esprit.jebi.ui.auth.AuthViewModel

import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ShoppingCart

import androidx.compose.material.icons.filled.Notifications
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CreditCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToAddWallet: () -> Unit,
    onNavigateToWalletDetail: (Int) -> Unit,
    onNavigateToTransactions: () -> Unit,
    onNavigateToBudgets: () -> Unit,
    onNavigateToReminders: () -> Unit,
    viewModel: WalletViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val wallets by viewModel.wallets.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard") },
                actions = {
                    IconButton(onClick = onNavigateToTransactions) {
                        Icon(Icons.Default.List, contentDescription = "Transactions")
                    }
                    IconButton(onClick = onNavigateToBudgets) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Budgets")
                    }
                    IconButton(onClick = onNavigateToReminders) {
                        Icon(Icons.Default.Notifications, contentDescription = "Reminders")
                    }
                    IconButton(onClick = { authViewModel.logout() }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Logout")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToAddWallet) {
                Icon(Icons.Default.Add, contentDescription = "Add Wallet")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(wallets) { wallet ->
                WalletItem(wallet = wallet, onClick = { onNavigateToWalletDetail(wallet.id) })
            }
        }
    }
}

@Composable
fun WalletItem(wallet: Wallet, onClick: () -> Unit) {
    val icon = when (wallet.type.lowercase()) {
        "cash" -> Icons.Default.AttachMoney
        "bank" -> Icons.Default.AccountBalanceWallet
        "card" -> Icons.Default.CreditCard
        else -> Icons.Default.AccountBalanceWallet
    }

    val cardColor = when (wallet.type.lowercase()) {
        "cash" -> Color(0xFF4CAF50) // Green
        "bank" -> Color(0xFF2196F3) // Blue
        "card" -> Color(0xFFFF9800) // Orange
        else -> MaterialTheme.colorScheme.surfaceVariant
    }

    val contentColor = if (wallet.type.lowercase() in listOf("cash", "bank", "card")) Color.White else MaterialTheme.colorScheme.onSurfaceVariant

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = wallet.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = contentColor.copy(alpha = 0.9f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${wallet.currency} ${wallet.balance}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = wallet.type.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = contentColor.copy(alpha = 0.7f)
                )
            }
        }
    }
}
