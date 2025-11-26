package com.esprit.jebi.ui.reminder

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esprit.jebi.data.local.entity.Reminder
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderScreen(
    navController: NavController,
    viewModel: ReminderViewModel = hiltViewModel()
) {
    val reminders by viewModel.reminders.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Reminders") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("add_reminder") }) {
                Icon(Icons.Default.Add, contentDescription = "Add Reminder")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(reminders) { reminder ->
                ReminderItem(
                    reminder = reminder,
                    onMarkAsPaid = { viewModel.markAsPaid(reminder) },
                    onDelete = { viewModel.deleteReminder(reminder) }
                )
            }
        }
    }
}

@Composable
fun ReminderItem(
    reminder: Reminder,
    onMarkAsPaid: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = reminder.title,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (reminder.isPaid) TextDecoration.LineThrough else null
                )
                Text(
                    text = "Due: ${SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(reminder.dueDate)}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "$${reminder.amount}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Row {
                if (!reminder.isPaid) {
                    IconButton(onClick = onMarkAsPaid) {
                        Icon(Icons.Default.Check, contentDescription = "Mark as Paid", tint = Color.Green)
                    }
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                }
            }
        }
    }
}
