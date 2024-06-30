package com.dezdeqness

import android.app.Application
import com.dezdeqness.di.AppComponent
import com.dezdeqness.di.DaggerAppComponent

class ShikimoriApp : Application() {

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
                .logInfo("ShikimoriApp", "Absord ${exception::class.simpleName}" ,exception)

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
