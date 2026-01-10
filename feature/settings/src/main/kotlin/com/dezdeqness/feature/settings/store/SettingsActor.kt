package com.dezdeqness.feature.settings.store

import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.feature.settings.store.actors.SectionActor
import com.dezdeqness.feature.settings.store.core.DeployEffect
import com.dezdeqness.feature.settings.store.core.HandleSettingClick
import com.dezdeqness.feature.settings.store.core.HandleSwitchChange
import com.dezdeqness.feature.settings.store.core.LoadInitialState
import com.dezdeqness.feature.settings.store.core.OnInitialStateLoaded
import com.dezdeqness.feature.settings.store.core.OnSettingUpdated
import com.dezdeqness.feature.settings.store.core.SettingsNamespace
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import money.vivid.elmslie.core.store.Actor
import javax.inject.Inject

class SettingsActor @Inject constructor(
    private val sectionActors: List<@JvmSuppressWildcards SectionActor>,
    private val appLogger: AppLogger,
) : Actor<SettingsNamespace.Command, SettingsNamespace.Event>() {

    override fun execute(command: SettingsNamespace.Command) = when (command) {
        is LoadInitialState -> flow {
            try {
                val allSettings = sectionActors.flatMap { it.buildSettings() }

                emit(OnInitialStateLoaded(allSettings))
            } catch (e: Throwable) {
                appLogger.logInfo(TAG, "Error loading initial state", e)
            }
        }

        is HandleSettingClick -> flow {
            try {
                val actor = sectionActors.firstOrNull {
                    it.canHandle(command.setting.sectionType)
                }

                if (actor != null) {
                    val result = actor.handleClick(command.id, command.setting)
                    result.updatedSettings?.let { settings ->
                        emit(OnSettingUpdated(settings.first()))
                    }

                    result.effect?.let { effect ->
                        emit(DeployEffect(effect))
                    }
                }
            } catch (e: Throwable) {
                appLogger.logInfo(TAG, "Error handling click for ${command.id}", e)
            }
        }

        is HandleSwitchChange -> flow {
            try {
                val actor = sectionActors.firstOrNull {
                    it.canHandle(command.setting.sectionType)
                }

                if (actor != null) {
                    val result = actor.handleSwitchChange(
                        command.id,
                        command.checked,
                        command.setting
                    )
                    result.updatedSettings?.let { settings ->
                        emit(OnSettingUpdated(settings.first()))
                    }
                }
            } catch (e: Throwable) {
                appLogger.logInfo(TAG, "Error handling switch change for ${command.id}", e)
            }
        }

        else -> emptyFlow()
    }

    companion object {
        private const val TAG = "SettingsActor"
    }
}
