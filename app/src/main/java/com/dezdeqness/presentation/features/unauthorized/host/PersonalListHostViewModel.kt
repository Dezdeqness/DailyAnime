package com.dezdeqness.presentation.features.unauthorized.host

import com.dezdeqness.contract.auth.repository.AuthRepository
import com.dezdeqness.core.AuthorizedUiState
import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.coroutines.CoroutineDispatcherProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class PersonalListHostViewModel @Inject constructor(
    private val authRepository: AuthRepository,
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
            val isAuthorized = authRepository.isAuthorized()
            launchOnMain {
                _hostStateFlow.value = UnauthorizedHostState(
                    authorizedState = if (isAuthorized) {
                        AuthorizedUiState.Authorized
                    } else {
                        AuthorizedUiState.Unauthorized
                    }
                )
            }
        }

        launchOnIo {
            authRepository.authorizationState().collect { state ->
                val isAuthorized = authRepository.isAuthorized()
                launchOnMain {
                    _hostStateFlow.value = UnauthorizedHostState(
                        authorizedState = if (isAuthorized) {
                            AuthorizedUiState.Authorized
                        } else {
                            AuthorizedUiState.Unauthorized
                        }
                    )
                }
            }
        }
    }

    override val viewModelTag = "PersonalListHostViewModel"

}
