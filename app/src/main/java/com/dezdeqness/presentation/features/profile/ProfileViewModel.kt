package com.dezdeqness.presentation.features.profile

import com.dezdeqness.contract.auth.SessionManager
import com.dezdeqness.contract.auth.model.SessionState
import com.dezdeqness.core.AuthorizedUiState
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.domain.usecases.GetUserUseCase
import com.dezdeqness.foundation.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.foundation.message.BaseMessageProvider
import com.dezdeqness.foundation.message.MessageConsumer
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProfileViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val sessionManager: SessionManager,
    private val messageConsumer: MessageConsumer,
    private val messageProvider: BaseMessageProvider,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    appLogger: AppLogger,
) : BaseViewModel(
    coroutineDispatcherProvider = coroutineDispatcherProvider,
    appLogger = appLogger,
),
    BaseViewModel.InitialLoaded {

    private val _profileStateFlow: MutableStateFlow<ProfileState> = MutableStateFlow(ProfileState())
    val profileStateFlow: StateFlow<ProfileState> get() = _profileStateFlow

    init {
        launchOnIo {
            sessionManager.sessionState.collect { state ->
                handleSessionState(state)
            }
        }
    }

    private fun handleSessionState(state: SessionState) {
        when (state) {
            is SessionState.Authenticated -> {
                fetchProfile()
            }
            is SessionState.Unauthenticated -> {
                _profileStateFlow.value = _profileStateFlow.value.copy(
                    authorizedState = AuthorizedUiState.Unauthorized,
                )
            }
            is SessionState.Loading -> {
                // no-op
            }
        }
    }

    override val viewModelTag = TAG

    override fun setLoadingIndicatorVisible(isVisible: Boolean) {
        // TODO
    }

    fun onLogoutClicked() {
        launchOnIo {
            sessionManager
                .logout()
                .onFailure {
                    messageConsumer.onErrorMessage(messageProvider.getGeneralErrorMessage())
                }
        }
    }

    private fun fetchProfile() {
        onInitialLoad(
            collector = getUserUseCase.invoke(),
            onSuccess = { account ->
                _profileStateFlow.value = _profileStateFlow.value.copy(
                    authorizedState = AuthorizedUiState.Authorized,
                    userId = account.id,
                    avatar = account.avatar,
                    nickname = account.nickname,
                )
            },
            onFailure = {
                launchOnIo {
                    messageConsumer.onErrorMessage(messageProvider.getGeneralErrorMessage())
                }
                logInfo("Error during fetch of profile on profile page", it)
            },
        )
    }

    companion object {
        private const val TAG = "ProfileViewModel"
    }
}
