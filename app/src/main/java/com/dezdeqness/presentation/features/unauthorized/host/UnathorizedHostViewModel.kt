package com.dezdeqness.presentation.features.unauthorized.host

import com.dezdeqness.core.AppLogger
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.CoroutineDispatcherProvider
import com.dezdeqness.domain.model.AuthorizationState
import com.dezdeqness.domain.repository.AccountRepository
import com.dezdeqness.presentation.event.Event
import com.dezdeqness.presentation.event.NavigateToPersonalList
import com.dezdeqness.presentation.event.NavigateToUnauthorized
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class UnathorizedHostViewModel @Inject constructor(
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
                val event = when (state) {
                    AuthorizationState.LoggedIn -> NavigateToPersonalList
                    AuthorizationState.LoggedOut -> NavigateToUnauthorized
                }

                launchOnMain {
                    _hostStateFlow.value = UnauthorizedHostState(
                        events = listOf(event),
                        isAuthorized = isAuthorized,
                    )
                }
            }
        }
    }

    override val viewModelTag = "PersonalListHostViewModel"

    override fun onEventConsumed(event: Event) {
        val value = _hostStateFlow.value
        _hostStateFlow.value = value.copy(
            events = value.events.toMutableList() - event
        )
    }

}
