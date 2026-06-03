package com.dezdeqness.feature.settings.store

import com.dezdeqness.feature.settings.store.actors.SectionActor
import com.dezdeqness.feature.settings.store.core.CloseDialog
import com.dezdeqness.feature.settings.store.core.DeployEffect
import com.dezdeqness.feature.settings.store.core.HandleSettingClick
import com.dezdeqness.feature.settings.store.core.HandleSwitchChange
import com.dezdeqness.feature.settings.store.core.LoadInitialState
import com.dezdeqness.feature.settings.store.core.OnInitialStateLoaded
import com.dezdeqness.feature.settings.store.core.OnSettingUpdated
import com.dezdeqness.feature.settings.store.core.SaveDialogResult
import com.dezdeqness.feature.settings.store.core.SettingsNamespace
import com.dezdeqness.feature.settings.store.core.ShowDialog
import com.dezdeqness.foundation.Logger
import javax.inject.Inject
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import money.vivid.elmslie.core.store.Actor

class SettingsActor @Inject constructor(
    private val sectionActors: List<@JvmSuppressWildcards SectionActor>,
    private val logger: Logger,
) : Actor<SettingsNamespace.Command, SettingsNamespace.Event>() {

    override fun execute(command: SettingsNamespace.Command) = when (command) {
        is LoadInitialState -> flow {
            try {
                val allSettings = sectionActors.flatMap { it.buildSettings() }

                emit(OnInitialStateLoaded(allSettings))
            } catch (e: Throwable) {
                logger.logInfo(TAG, "Error loading initial state", e)
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
                        settings.forEach { updated ->
                            emit(OnSettingUpdated(updated))
                        }
                    }

                    result.dialog?.let { dialog ->
                        emit(ShowDialog(dialog))
                    }

                    result.effect?.let { effect ->
                        emit(DeployEffect(effect))
                    }
                }
            } catch (e: Throwable) {
                logger.logInfo(TAG, "Error handling click for ${command.id}", e)
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
                        command.setting,
                    )
                    result.updatedSettings?.let { settings ->
                        settings.forEach { updated ->
                            emit(OnSettingUpdated(updated))
                        }
                    }

                    result.effect?.let { effect ->
                        emit(DeployEffect(effect))
                    }
                }
            } catch (e: Throwable) {
                logger.logInfo(TAG, "Error handling switch change for ${command.id}", e)
            }
        }

        is SaveDialogResult -> flow {
            try {
                val actor = sectionActors.firstOrNull {
                    it.canHandle(command.currentSetting.sectionType)
                }

                if (actor != null) {
                    val result = actor.saveDialogResult(
                        settingId = command.id,
                        data = command.data,
                        currentSetting = command.currentSetting,
                    )

                    result.updatedSettings?.let { settings ->
                        settings.forEach { updated ->
                            emit(OnSettingUpdated(updated))
                        }
                    }

                    result.effect?.let { effect ->
                        emit(DeployEffect(effect))
                    }
                }

                emit(CloseDialog)
            } catch (e: Throwable) {
                logger.logInfo(TAG, "Error saving dialog result for ${command.id}", e)
            }
        }

        else -> emptyFlow()
    }

    companion object {
        private const val TAG = "SettingsActor"
    }
}
