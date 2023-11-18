package com.dezdeqness.presentation.features.unauthorized

import com.dezdeqness.core.AppLogger
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.CoroutineDispatcherProvider
import com.dezdeqness.domain.usecases.LoginUseCase
import com.dezdeqness.presentation.event.Event
import javax.inject.Inject

class UnauthorizedViewModel @Inject constructor(
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    appLogger: AppLogger,
) : BaseViewModel(
    coroutineDispatcherProvider = coroutineDispatcherProvider,
    appLogger = appLogger,
) {
    override val viewModelTag = "UnauthorizedViewModel"

    override fun onEventConsumed(event: Event) {

    }

    fun onAuthorizationCodeReceived(code: String?) {
        if (code.isNullOrEmpty()) {
            // TODO: error prompt
            appLogger.logInfo(viewModelTag, "code is empty")
            return
        }
        launchOnIo {
        }
    }

}
