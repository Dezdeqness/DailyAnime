package com.dezdeqness.presentation.features.routing

import com.dezdeqness.contract.auth.repository.AuthRepository
import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.WorkSchedulerManager
import com.dezdeqness.core.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.data.core.config.ConfigManager
import com.dezdeqness.data.provider.PermissionCheckProvider
import com.dezdeqness.contract.user.repository.UserRepository
import com.dezdeqness.presentation.event.HandlePermission
import com.dezdeqness.presentation.event.NavigateToMainFlow
import javax.inject.Inject

class RoutingViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val configManager: ConfigManager,
    private val workSchedulerManager: WorkSchedulerManager,
    permissionCheckProvider: PermissionCheckProvider,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    appLogger: AppLogger,
) : BaseViewModel(
    coroutineDispatcherProvider = coroutineDispatcherProvider,
    appLogger = appLogger,
) {
    override val viewModelTag: String = TAG

    init {
        if (permissionCheckProvider.isNotificationPermissionGranted()) {
            fetchData()
        } else {
            onEventReceive(HandlePermission)
        }
    }

    fun fetchData() {
        launchOnIo {
            workSchedulerManager.scheduleDailyWork()
            configManager.invalidate()

            if (authRepository.isAuthorized()) {
                userRepository
                    .getProfileRemote()
                    .onSuccess {
                        userRepository.saveProfileLocal(it)

                        onEventReceive(NavigateToMainFlow)
                    }
                    .onFailure {
                        // TODO: Retry
                        logInfo("Error during fetch of profile on splash page", it)

                        onEventReceive(NavigateToMainFlow)

                    }
            } else {
                onEventReceive(NavigateToMainFlow)
            }
        }
    }

    companion object {
        private const val TAG = "RoutingViewModel"
    }

}
