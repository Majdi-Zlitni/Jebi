package com.esprit.jebi.worker

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.esprit.jebi.R
import com.esprit.jebi.data.repository.ReminderRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class ReminderWorker @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val reminderRepository: ReminderRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val reminders = reminderRepository.getUnpaidReminders().first()
        val currentTime = System.currentTimeMillis()
        
        reminders.forEach { reminder ->
            // Check if due within 24 hours
            if (reminder.dueDate - currentTime < 24 * 60 * 60 * 1000 && reminder.dueDate > currentTime) {
                showNotification(reminder.title, "Due soon: $${reminder.amount}")
            } else if (reminder.dueDate < currentTime) {
                showNotification(reminder.title, "Overdue: $${reminder.amount}")
            }
        }

        return Result.success()
    }

    private fun showNotification(title: String, content: String) {
        if (ActivityCompat.checkSelfPermission(
                appContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        
        val builder = NotificationCompat.Builder(appContext, "jebi_channel_id")
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Replace with app icon
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(appContext)) {
            notify(System.currentTimeMillis().toInt(), builder.build())
        }
    }
}
