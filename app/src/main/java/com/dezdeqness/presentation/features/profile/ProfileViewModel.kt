package com.dezdeqness.presentation.features.profile

import com.dezdeqness.core.AppLogger
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.CoroutineDispatcherProvider
import com.dezdeqness.domain.repository.AccountRepository
import com.dezdeqness.domain.usecases.GetProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
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

        launchOnIo {
            accountRepository.authorizationState().collect { state ->
                val isAuthorized = accountRepository.isAuthorized()

                if (isAuthorized) {
                    fetchProfile()
                }
            }
        }

    }

    override val viewModelTag = "ProfileViewModel"

    override fun setLoadingIndicatorVisible(isVisible: Boolean) {
        // TODO
    }

    private fun fetchProfile() {
        onInitialLoad(
            collector = getProfileUseCase.invoke(),
            onSuccess = { account ->
                _profileStateFlow.value = _profileStateFlow.value.copy(
                    isAuthorized = true,
                    avatar = account.avatar,
                    nickname = account.nickname,
                )
            },
            onFailure = {
                logInfo("Error during fetch of profile on profile page", it)
            }
        )
    }

    companion object {
        private const val TAG = "ProfileViewModel"
    }

}
