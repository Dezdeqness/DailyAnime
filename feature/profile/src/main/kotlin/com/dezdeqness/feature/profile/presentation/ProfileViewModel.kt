package com.dezdeqness.feature.profile.presentation

import androidx.lifecycle.viewModelScope
import com.dezdeqness.contract.auth.SessionManager
import com.dezdeqness.contract.auth.model.SessionState
import com.dezdeqness.contract.user.repository.UserRepository
import com.dezdeqness.foundation.BaseViewModel
import com.dezdeqness.foundation.Logger
import com.dezdeqness.foundation.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.foundation.message.BaseMessageProvider
import com.dezdeqness.foundation.message.MessageConsumer
import com.dezdeqness.shared.presentation.model.AuthorizedUiState
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn

class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager,
    private val messageConsumer: MessageConsumer,
    private val messageProvider: BaseMessageProvider,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    logger: Logger,
) : BaseViewModel(coroutineDispatcherProvider, logger) {

    override val viewModelTag = "ProfileViewModel"

    @OptIn(ExperimentalCoroutinesApi::class)
    val profileStateFlow: StateFlow<ProfileState> =
        sessionManager.sessionState
            .flatMapLatest { state -> profileStateFor(state) }
            .catch {
                logInfo("Error in profile flow", it)
                messageConsumer.onErrorMessage(messageProvider.getGeneralErrorMessage())
            }
            .flowOn(coroutineDispatcherProvider.io())
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = ProfileState(),
            )

    fun onLogoutClicked() {
        launchOnIo {
            sessionManager
                .logout()
                .onFailure {
                    messageConsumer.onErrorMessage(messageProvider.getGeneralErrorMessage())
                }
        }
    }

    private fun profileStateFor(state: SessionState): Flow<ProfileState> = when (state) {
        is SessionState.Authenticated -> flow {
            userRepository.getProfileDetails().collect { result ->
                result
                    .onSuccess { account ->
                        emit(
                            ProfileState(
                                authorizedState = AuthorizedUiState.Authorized,
                                userId = account.id,
                                avatar = account.avatar,
                                nickname = account.nickname,
                            ),
                        )
                    }
                    .onFailure {
                        logInfo("Error during fetch of profile on profile page", it)
                        messageConsumer.onErrorMessage(messageProvider.getGeneralErrorMessage())
                    }
            }
        }

        is SessionState.Unauthenticated -> flowOf(
            ProfileState(authorizedState = AuthorizedUiState.Unauthorized),
        )

        is SessionState.Loading -> emptyFlow()
    }
}
