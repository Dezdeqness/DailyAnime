package com.dezdeqness.presentation.features.unauthorized.host

import com.dezdeqness.core.AuthorizedUiState
import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.domain.repository.AccountRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class PersonalListHostViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
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
            val isAuthorized = accountRepository.isAuthorized()
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
            accountRepository.authorizationState().collect { state ->
                val isAuthorized = accountRepository.isAuthorized()
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
