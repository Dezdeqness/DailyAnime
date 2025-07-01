package com.dezdeqness.data.core.config.remote

import com.dezdeqness.data.BuildConfig
import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.data.core.config.BaseConfigProvider
import com.dezdeqness.data.core.config.ConfigKeys
import com.google.android.gms.tasks.Task
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.resume

class RemoteConfigProvider(
    private val appLogger: AppLogger,
) : BaseConfigProvider {

    val configSettings = remoteConfigSettings {
        minimumFetchIntervalInSeconds = if (BuildConfig.DEBUG) 0 else 3600 // 1 hour
    }

    private val remoteConfig = Firebase.remoteConfig.apply {
        setConfigSettingsAsync(configSettings)
    }

    suspend fun refresh() {
        remoteConfig
            .fetchAndActivate()
            .await()
            .onSuccess {
                val task = remoteConfig.activate().await()
                appLogger.logInfo(TAG, "fetchConfig is ${task.isSuccess}")
            }
    }

    fun setDefaults() {
        runBlocking {
            remoteConfig
                .setDefaultsAsync(ConfigKeys.defaults())
                .await()
                .onSuccess {
                    appLogger.logInfo(TAG, "Default async success")
                }
                .onFailure {
                    appLogger.logInfo(TAG, "Default async error", it)
                }
        }
    }

    override fun getStringValue(key: String) = remoteConfig.getValue(key).asString()
    override fun getIntValue(key: String) = remoteConfig.getValue(key).asLong().toInt()
    override fun getDoubleValue(key: String) = remoteConfig.getValue(key).asDouble()
    override fun getBooleanValue(key: String) = remoteConfig.getValue(key).asBoolean()

    private suspend fun <T> Task<T>.await(): Result<T> {
        if (isComplete) {
            val e = exception
            return if (e == null) {
                if (isCanceled) {
                    Result.failure(CancellationException("Task $this was cancelled normally."))
                } else {
                    Result.success(result)
                }
            } else {
                Result.failure(e)
            }
        }

        return suspendCancellableCoroutine { cont ->
            addOnCompleteListener {
                val e = exception
                if (e == null) {
                    if (isCanceled) cont.cancel() else cont.resume(Result.success(result))
                } else {
                    cont.resume(Result.failure(e))
                }
            }
        }
    }

    companion object {
        private const val TAG = "RemoteConfigProvider"
    }

}
