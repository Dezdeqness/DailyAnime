package com.dezdeqness.presentation.features.profile

import com.dezdeqness.contract.auth.repository.AuthRepository
import com.dezdeqness.core.AuthorizedUiState
import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.MessageProvider
import com.dezdeqness.core.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.contract.user.usecase.GetUserUseCase
import com.dezdeqness.contract.auth.usecase.LogoutUseCase
import com.dezdeqness.presentation.message.MessageConsumer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val authRepository: AuthRepository,
    private val messageConsumer: MessageConsumer,
    private val messageProvider: MessageProvider,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    appLogger: AppLogger,
) : BaseViewModel(
    coroutineDispatcherProvider = coroutineDispatcherProvider,
    appLogger = appLogger,
), BaseViewModel.InitialLoaded {

    private val _profileStateFlow: MutableStateFlow<ProfileState> = MutableStateFlow(ProfileState())
    val profileStateFlow: StateFlow<ProfileState> get() = _profileStateFlow

    init {
        launchOnIo {
            handleProfileState()
        }

        launchOnIo {
            authRepository.authorizationState().collect { state ->
                handleProfileState()
            }
        }

    }

    private fun handleProfileState() {
        val isAuthorized = authRepository.isAuthorized()
        if (isAuthorized) {
            fetchProfile()
        } else {
            _profileStateFlow.value = _profileStateFlow.value.copy(
                authorizedState = AuthorizedUiState.Unauthorized,
            )
        }
    }

    override val viewModelTag = TAG

    override fun setLoadingIndicatorVisible(isVisible: Boolean) {
        // TODO
    }

    fun onLogoutClicked() {
        launchOnIo {
            logoutUseCase
                .invoke()
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
                    avatar = account.avatar,
                    nickname = account.nickname,
                )
            },
            onFailure = {
                launchOnIo {
                    messageConsumer.onErrorMessage(messageProvider.getGeneralErrorMessage())
                }
                logInfo("Error during fetch of profile on profile page", it)
            }
        )
    }

    companion object {
        private const val TAG = "ProfileViewModel"
    }

}
