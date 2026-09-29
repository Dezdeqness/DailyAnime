package com.dezdeqness.core

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.dezdeqness.appComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val workScheduler = context.appComponent.workSchedulerManager

            CoroutineScope(Dispatchers.Default).launch {
                workScheduler.scheduleDailyWork()
            }
        }
    }
}
