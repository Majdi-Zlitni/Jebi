package com.esprit.jebi.ui.budget

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetScreen(
    navController: NavController,
    viewModel: BudgetViewModel = hiltViewModel()
) {
    val budgets by viewModel.budgets.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Budgets") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("add_budget") }) {
                Icon(Icons.Default.Add, contentDescription = "Add Budget")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(budgets) { budgetWithProgress ->
                BudgetItem(budgetWithProgress)
            }
        }
    }
}

@Composable
fun BudgetItem(item: BudgetWithProgress) {
    val progress = (item.spent / item.budget.amount).toFloat().coerceIn(0f, 1f)
    val isOverBudget = item.spent > item.budget.amount

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = item.budget.category, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "$${item.spent} / $${item.budget.amount}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isOverBudget) Color.Red else MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier.fillMaxWidth(),
                color = if (isOverBudget) Color.Red else MaterialTheme.colorScheme.primary
            )
        }
    }
}
