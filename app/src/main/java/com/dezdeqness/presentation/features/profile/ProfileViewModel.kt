package com.dezdeqness.presentation.features.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dezdeqness.domain.repository.AccountRepository
import com.dezdeqness.domain.usecases.GetProfileUseCase
import com.dezdeqness.domain.usecases.LoginUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val accountRepository: AccountRepository,
) : ViewModel() {

    private val _profileStateFlow: MutableStateFlow<ProfileState> = MutableStateFlow(ProfileState())
    val profileStateFlow: StateFlow<ProfileState> get() = _profileStateFlow

    init {
        viewModelScope.launch(Dispatchers.IO) {
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

    fun onAuthorizationCodeReceived(code: String?) {
        if (code.isNullOrEmpty()) {
            // TODO: error prompt
            Log.d("ProfileViewModel", "code is empty")
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            loginUseCase
                .invoke(code)
                .onSuccess {
                    fetchProfile()
                }
                .onFailure {
                    Log.d("ProfileViewModel", "login failed")
                }
        }
    }

    private fun fetchProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            getProfileUseCase
                .invoke()
                .collect { result ->
                    result
                        .onSuccess { account ->
                            _profileStateFlow.value = _profileStateFlow.value.copy(
                                isAuthorized = true,
                                avatar = account.avatar,
                                nickname = account.nickname,
                            )
                        }.onFailure {
                            Log.d("ProfileViewModel", "fetchProfile failed")
                        }
                }
        }
    }

}
