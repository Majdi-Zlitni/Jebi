package com.esprit.jebi.data.repository

import com.esprit.jebi.data.local.dao.ReminderDao
import com.esprit.jebi.data.local.entity.Reminder
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReminderRepository @Inject constructor(
    private val reminderDao: ReminderDao
) {
    fun getAllReminders(): Flow<List<Reminder>> = reminderDao.getAllReminders()
    
    fun getUnpaidReminders(): Flow<List<Reminder>> = reminderDao.getUnpaidReminders()

    suspend fun insertReminder(reminder: Reminder) = reminderDao.insertReminder(reminder)

    suspend fun updateReminder(reminder: Reminder) = reminderDao.updateReminder(reminder)

    suspend fun deleteReminder(reminder: Reminder) = reminderDao.deleteReminder(reminder)
}
