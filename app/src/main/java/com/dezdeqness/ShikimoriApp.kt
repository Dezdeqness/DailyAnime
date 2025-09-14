package com.dezdeqness

import android.app.Application
import android.os.Build
import coil.Coil
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.disk.DiskCache
import coil.util.DebugLogger
import com.dezdeqness.contract.settings.models.ImageCacheMaxSizePreference
import com.dezdeqness.di.AppComponent
import com.dezdeqness.di.DaggerAppComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext

class ShikimoriApp : Application(), CoroutineScope {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

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

        Coil.setImageLoader(
            createImageLoader(
                runBlocking {
                    appComponent.settingsRepository.getPreference(ImageCacheMaxSizePreference)
                },
            ),
        )

        launch {
            appComponent.settingsRepository
                .observePreference(ImageCacheMaxSizePreference)
                .drop(1)
                .collect { cacheMb ->
                    Coil.setImageLoader(createImageLoader(cacheMb))
                }
        }
    }

    private fun createImageLoader(cacheSizeMb: Int) = ImageLoader.Builder(this)
        .allowHardware(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
        .components {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .logger(DebugLogger())
        .diskCache(
            DiskCache.Builder()
                .directory(cacheDir.resolve("coil"))
                .maxSizeBytes(cacheSizeMb * 1024 * 1024L)
                .build()
        )
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
