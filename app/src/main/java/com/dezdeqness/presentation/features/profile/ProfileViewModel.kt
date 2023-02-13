package com.dezdeqness.presentation.features.profile

import com.dezdeqness.core.AppLogger
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.CoroutineDispatcherProvider
import com.dezdeqness.domain.repository.AccountRepository
import com.dezdeqness.domain.usecases.GetProfileUseCase
import com.dezdeqness.domain.usecases.LoginUseCase
import com.dezdeqness.presentation.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val accountRepository: AccountRepository,
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
            val isAuthorized = accountRepository.isAuthorized()
            if (isAuthorized) {
                fetchProfile()
            } else {
                _profileStateFlow.value = _profileStateFlow.value.copy(
                    isAuthorized = false,
                )
            }
        }
    }

    override fun viewModelTag() = "ProfileViewModel"

    override fun onEventConsumed(event: Event) {
        // TODO
    }

    override fun setLoadingIndicatorVisible(isVisible: Boolean) {
        // TODO
    }

    fun onAuthorizationCodeReceived(code: String?) {
        if (code.isNullOrEmpty()) {
            // TODO: error prompt
            appLogger.logInfo(viewModelTag(), "code is empty")
            return
        }
        launchOnIo {
            loginUseCase
                .invoke(code)
                .onSuccess {
                    fetchProfile()
                }
                .onFailure {
                    appLogger.logInfo(
                        tag = viewModelTag(),
                        throwable = it,
                        message = "login failed",
                    )
                }
        }
    }

    private fun fetchProfile() {
        onInitialLoad(
            collector = getProfileUseCase(),
            onSuccess = { account ->
                _profileStateFlow.value = _profileStateFlow.value.copy(
                    isAuthorized = true,
                    avatar = account.avatar,
                    nickname = account.nickname,
                )
            },
        )
    }

}
