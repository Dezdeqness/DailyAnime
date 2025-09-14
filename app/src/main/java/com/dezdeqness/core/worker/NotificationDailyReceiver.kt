package com.dezdeqness.core.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.dezdeqness.R
import com.dezdeqness.ShikimoriApp
import com.dezdeqness.contract.settings.models.NotificationEnabledPreference
import com.dezdeqness.data.provider.PermissionCheckProvider
import com.dezdeqness.getComponent
import kotlinx.coroutines.runBlocking

class NotificationDailyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        showNotification(context)
    }

    private fun showNotification(context: Context) {
        val application = context.applicationContext as ShikimoriApp
        val settingsRepository = application.getComponent().settingsRepository
        val permissionCheckProvider: PermissionCheckProvider =
            application.getComponent().permissionCheckProvider

        val isNotificationEnabled = runBlocking {
            settingsRepository.getPreference(NotificationEnabledPreference)
        }
        val permissionGranted = permissionCheckProvider.isNotificationPermissionGranted()

        if (isNotificationEnabled && permissionGranted) {
            createNotificationChannel(context)

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

    private fun createNotificationChannel(context: Context) {
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
