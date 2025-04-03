package com.dezdeqness

import android.app.Application
import android.content.Context
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.dezdeqness.core.worker.NotificationDailyWorker
import com.dezdeqness.di.AppComponent
import com.dezdeqness.di.DaggerAppComponent

class ShikimoriApp : Application(), Configuration.Provider {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()
        Thread
            .getDefaultUncaughtExceptionHandler()
            ?.let { defaultUncaughtExceptionHandler ->
                Thread.setDefaultUncaughtExceptionHandler(
                    CustomUncaughtExceptionHandler(
                        application = this,
                        defaultUncaughtExceptionHandler,
                    )
                )
            }
    }

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(object : WorkerFactory() {
                override fun createWorker(
                    appContext: Context,
                    workerClassName: String,
                    workerParameters: WorkerParameters
                ): ListenableWorker? {
                    val workerClass = try {
                        Class.forName(workerClassName).asSubclass(Worker::class.java)
                    } catch (_: ClassNotFoundException) {
                        null
                    }
                    return if (workerClass == NotificationDailyWorker::class.java) {
                        NotificationDailyWorker(
                            settingsRepository = appComponent.settingsRepository,
                            permissionCheckProvider = appComponent.permissionCheckProvider,
                            appContext,
                            workerParameters)
                    } else {
                        null
                    }
                }

            })
            .build()

}

fun Application.getComponent(): AppComponent {
    return (this as ShikimoriApp).appComponent
}

// Taken from
// https://stackoverflow.com/questions/72902856/cannotdeliverbroadcastexception-only-on-pixel-devices-running-android-12
// Suppress issue related CannotDeliverBroadcastException
private class CustomUncaughtExceptionHandler(
    private val application: Application,
    private val uncaughtExceptionHandler: Thread.UncaughtExceptionHandler
) : Thread.UncaughtExceptionHandler {

    override fun uncaughtException(thread: Thread, exception: Throwable) {
        if (shouldAbsorb(exception)) {
            application
                .getComponent()
                .appLogger
                .logInfo("ShikimoriApp", "Absord ${exception::class.simpleName}", exception)

            return
        }
        uncaughtExceptionHandler.uncaughtException(thread, exception)
    }

    /**
     * Evaluate whether to silently absorb uncaught crashes such that they
     * don't crash the app. We generally want to avoid this practice - we would
     * rather know about them. However in some cases there's nothing we can do
     * about the crash (e.g. it is an OS fault) and we would rather not have them
     * pollute our reliability stats.
     */
    private fun shouldAbsorb(exception: Throwable): Boolean {
        return when (exception::class.simpleName) {

            "CannotDeliverBroadcastException" -> true

            else -> false
        }
    }
}
