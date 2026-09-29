package com.dezdeqness

import android.app.Application
import android.content.Context
import android.os.Build
import coil.Coil
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.disk.DiskCache
import coil.util.DebugLogger
import com.dezdeqness.contract.settings.models.ImageCacheMaxSizePreference
import com.dezdeqness.contract.settings.models.SourceTypePreference
import com.dezdeqness.contract.source.SourceType
import com.dezdeqness.di.AppComponent
import com.dezdeqness.di.DaggerAppComponent
import com.dezdeqness.di.source.AvailableSources
import com.dezdeqness.di.source.SourceComponent
import com.dezdeqness.shared.presentation.bridge.ApplicationBridge
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ShikimoriApp : Application(), CoroutineScope, ApplicationBridge {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }

    private var currentSourceComponent: SourceComponent? = null

    val sourceComponent: SourceComponent
        get() = currentSourceComponent ?: createSourceComponent(storedSourceType()).also { currentSourceComponent = it }

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
                    ),
                )
            }

        launch(appComponent.coroutineDispatcherProvider().io()) {
            appComponent.configManager.invalidate()
        }

        restoreSession(sourceComponent)

        launch {
            Coil.setImageLoader(
                createImageLoader(
                    appComponent.settingsRepository.getPreference(ImageCacheMaxSizePreference),
                ),
            )
        }

        launch {
            appComponent.settingsRepository
                .observePreference(ImageCacheMaxSizePreference)
                .drop(1)
                .collect { cacheMb ->
                    Coil.setImageLoader(createImageLoader(cacheMb))
                }
        }
    }

    fun rebuildSourceComponent() {
        currentSourceComponent = createSourceComponent(storedSourceType()).also(::restoreSession)
    }

    private fun restoreSession(component: SourceComponent) {
        launch(appComponent.coroutineDispatcherProvider().io()) {
            component.sessionManager.restoreSession()
        }
    }

    private fun storedSourceType(): SourceType {
        val stored = runBlocking { appComponent.settingsRepository.getPreference(SourceTypePreference) }
        return stored.takeIf { it in AvailableSources.all } ?: SourceType.SHIKIMORI
    }

    private fun createSourceComponent(sourceType: SourceType): SourceComponent = when (sourceType) {
        SourceType.SHIKIMORI -> appComponent.shikimoriComponent().create()
        SourceType.ANILIST -> error("AniList source component is not wired yet")
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
                .build(),
        )
        .build()

    override fun getSettingsRepository() = appComponent.settingsRepository

    override fun getPermissionCheckProvider() = appComponent.permissionCheckProvider

    override fun getAppForegroundIcon() = R.drawable.ic_launcher_foreground

    override fun getVersionName() = BuildConfig.VERSION_NAME

    override fun isDebug() = BuildConfig.DEBUG
}

val Context.appComponent: AppComponent
    get() = (applicationContext as ShikimoriApp).appComponent

val Context.sourceComponent: SourceComponent
    get() = (applicationContext as ShikimoriApp).sourceComponent

fun Context.rebuildSourceComponent() = (applicationContext as ShikimoriApp).rebuildSourceComponent()

// Taken from
// https://stackoverflow.com/questions/72902856/cannotdeliverbroadcastexception-only-on-pixel-devices-running-android-12
// Suppress issue related CannotDeliverBroadcastException
private class CustomUncaughtExceptionHandler(
    private val application: Application,
    private val uncaughtExceptionHandler: Thread.UncaughtExceptionHandler,
) : Thread.UncaughtExceptionHandler {

    override fun uncaughtException(thread: Thread, exception: Throwable) {
        if (shouldAbsorb(exception)) {
            application
                .appComponent
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
