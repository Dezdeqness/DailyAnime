package com.dezdeqness.core.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.dezdeqness.R
import com.dezdeqness.data.provider.PermissionCheckProvider
import com.dezdeqness.domain.repository.SettingsRepository
import kotlinx.coroutines.runBlocking

class NotificationDailyWorker(
    private val settingsRepository: SettingsRepository,
    private val permissionCheckProvider: PermissionCheckProvider,
    private val context: Context,
    workerParams: WorkerParameters,
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        showNotification()
        return Result.success()
    }

    private fun showNotification() {
        val isNotificationEnabled = runBlocking {
            settingsRepository.getNotificationsEnabled()
        }
        val permissionGranted = permissionCheckProvider.isNotificationPermissionGranted()

        if (isNotificationEnabled && permissionGranted) {
            createNotificationChannel()

            val title = context.getString(R.string.app_name)
            val message = context.getString(R.string.notification_daily_description)

            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(NOTIFICATION_ID, builder.build())
        }

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Daily Channel"
            val descriptionText = "Channel for scheduled daily notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val CHANNEL_ID = "notification_daily_channel"
        private const val NOTIFICATION_ID = 236
    }
}

