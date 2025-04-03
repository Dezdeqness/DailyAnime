package com.dezdeqness.core

import android.content.Context
import android.util.Log
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.dezdeqness.core.worker.NotificationDailyWorker
import com.dezdeqness.domain.repository.SettingsRepository
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class WorkSchedulerManager @Inject constructor(
    private val context: Context,
    private val settingsRepository: SettingsRepository,
) {

    suspend fun scheduleDailyWork() {
        val time = settingsRepository.getNotificationTime()

        val currentTime = Calendar.getInstance()

        val targetTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, time.hours)
            set(Calendar.MINUTE, time.minutes)
            set(Calendar.SECOND, 0)
        }

        var initialDelay = targetTime.timeInMillis - currentTime.timeInMillis
        if (initialDelay < 0) {
            initialDelay += TimeUnit.DAYS.toMillis(1)
        }

        val periodicWorkRequest =
            PeriodicWorkRequestBuilder<NotificationDailyWorker>(24, TimeUnit.HOURS)
                .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                .build()

        WorkManager
            .getInstance(context)
            .enqueueUniquePeriodicWork(
                DAILY_NOTIFICATION_WORK_NAME,
                ExistingPeriodicWorkPolicy.REPLACE,
                periodicWorkRequest
            )
    }

    companion object {
        private const val DAILY_NOTIFICATION_WORK_NAME = "DailyWorkerJob"
    }
}
