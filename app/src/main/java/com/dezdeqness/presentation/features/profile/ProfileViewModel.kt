package com.dezdeqness.presentation.features.profile

import com.dezdeqness.core.AuthorizedUiState
import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.MessageProvider
import com.dezdeqness.core.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.domain.repository.AccountRepository
import com.dezdeqness.domain.usecases.GetProfileUseCase
import com.dezdeqness.domain.usecases.LogoutUseCase
import com.dezdeqness.presentation.message.MessageConsumer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val accountRepository: AccountRepository,
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
            accountRepository.authorizationState().collect { state ->
                handleProfileState()
            }
        }

    }

    private fun handleProfileState() {
        val isAuthorized = accountRepository.isAuthorized()
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
            collector = getProfileUseCase.invoke(),
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
