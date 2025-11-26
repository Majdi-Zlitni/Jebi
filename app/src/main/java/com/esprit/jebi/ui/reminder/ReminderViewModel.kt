package com.esprit.jebi.ui.reminder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esprit.jebi.data.local.entity.Reminder
import com.esprit.jebi.data.repository.ReminderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val reminderRepository: ReminderRepository
) : ViewModel() {

    val reminders: StateFlow<List<Reminder>> = reminderRepository.getAllReminders()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addReminder(title: String, amount: Double, dueDate: Long) {
        viewModelScope.launch {
            val reminder = Reminder(
                title = title,
                amount = amount,
                dueDate = dueDate
            )
            reminderRepository.insertReminder(reminder)
        }
    }

    fun markAsPaid(reminder: Reminder) {
        viewModelScope.launch {
            reminderRepository.updateReminder(reminder.copy(isPaid = true))
        }
    }
    
    fun deleteReminder(reminder: Reminder) {
        viewModelScope.launch {
            reminderRepository.deleteReminder(reminder)
        }
    }
}
