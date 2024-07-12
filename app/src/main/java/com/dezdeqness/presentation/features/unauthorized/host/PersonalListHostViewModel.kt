package com.dezdeqness.presentation.features.unauthorized.host

import com.dezdeqness.core.AppLogger
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.CoroutineDispatcherProvider
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
                _hostStateFlow.value = UnauthorizedHostState(isAuthorized = isAuthorized)
            }
        }

        launchOnIo {
            accountRepository.authorizationState().collect { state ->
                val isAuthorized = accountRepository.isAuthorized()
                launchOnMain {
                    _hostStateFlow.value = UnauthorizedHostState(
                        isAuthorized = isAuthorized,
                    )
                }
            }
        }
    }

    override val viewModelTag = "PersonalListHostViewModel"

}
