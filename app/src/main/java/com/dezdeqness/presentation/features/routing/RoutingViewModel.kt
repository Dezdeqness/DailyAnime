package com.dezdeqness.presentation.features.routing

import com.dezdeqness.contract.auth.SessionManager
import com.dezdeqness.contract.auth.model.SessionState
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.foundation.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.presentation.event.HandlePermission
import com.dezdeqness.presentation.event.NavigateToMainFlow
import com.dezdeqness.shared.presentation.manager.WorkSchedulerManager
import com.dezdeqness.shared.presentation.provider.PermissionCheckProvider
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class RoutingViewModel @Inject constructor(
    private val sessionManager: SessionManager,
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

            sessionManager.sessionState.first { it !is SessionState.Loading }

            onEventReceive(NavigateToMainFlow)
        }
    }

    companion object {
        private const val TAG = "RoutingViewModel"
    }

}
