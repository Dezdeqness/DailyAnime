package com.dezdeqness.core

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.dezdeqness.core.worker.NotificationDailyReceiver
import com.dezdeqness.domain.repository.SettingsRepository
import java.util.Calendar
import javax.inject.Inject

class WorkSchedulerManager @Inject constructor(
    private val context: Context,
    private val settingsRepository: SettingsRepository,
) {

    suspend fun scheduleDailyWork() {
        val time = settingsRepository.getNotificationTime()

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, NotificationDailyReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, time.hours)
            set(Calendar.MINUTE, time.minutes)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            if (before(Calendar.getInstance())) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }

}
