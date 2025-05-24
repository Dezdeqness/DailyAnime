package com.dezdeqness.core

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.dezdeqness.ShikimoriApp
import com.dezdeqness.getComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val application = context.applicationContext as ShikimoriApp
            val workScheduler = application.getComponent().workSchedulerManager

            CoroutineScope(Dispatchers.Default).launch {
                workScheduler.scheduleDailyWork()
            }
        }
    }
}
