package com.dezdeqness.presentation.features.routing

import com.dezdeqness.core.AppLogger
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.CoroutineDispatcherProvider
import com.dezdeqness.domain.repository.AccountRepository
import com.dezdeqness.presentation.event.NavigateToMainFlow
import javax.inject.Inject

class RoutingViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    appLogger: AppLogger,
) : BaseViewModel(
    coroutineDispatcherProvider = coroutineDispatcherProvider,
    appLogger = appLogger,
) {
    override val viewModelTag: String = TAG


    init {
        launchOnIo {
            if (accountRepository.isAuthorized()) {
                accountRepository
                    .getProfileRemote()
                    .onSuccess {
                        accountRepository.saveProfileLocal(it)

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
