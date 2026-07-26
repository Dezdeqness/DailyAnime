package com.dezdeqness.presentation.features.unauthorized.host

import com.dezdeqness.contract.auth.SessionManager
import com.dezdeqness.contract.auth.model.SessionState
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.foundation.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.shared.presentation.model.AuthorizedUiState
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PersonalListHostViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    appLogger: AppLogger,
) : BaseViewModel(
    coroutineDispatcherProvider = coroutineDispatcherProvider,
    appLogger = appLogger,
) {

    private val _hostStateFlow: MutableStateFlow<UnauthorizedHostState> =
        MutableStateFlow(UnauthorizedHostState())
    val hostStateFlow: StateFlow<UnauthorizedHostState> get() = _hostStateFlow

    init {
        launchOnIo {
            sessionManager.sessionState.collect { state ->
                val authorizedState = when (state) {
                    is SessionState.Authenticated -> AuthorizedUiState.Authorized
                    is SessionState.Unauthenticated -> AuthorizedUiState.Unauthorized
                    is SessionState.Loading -> AuthorizedUiState.Pending
                }
                launchOnMain {
                    _hostStateFlow.value = UnauthorizedHostState(
                        authorizedState = authorizedState,
                    )
                }
            }
        }
    }

    override val viewModelTag = "PersonalListHostViewModel"
}
