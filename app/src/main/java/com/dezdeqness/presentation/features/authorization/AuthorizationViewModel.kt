package com.dezdeqness.presentation.features.authorization

import com.dezdeqness.core.AppLogger
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.CoroutineDispatcherProvider
import com.dezdeqness.domain.repository.AccountRepository
import com.dezdeqness.domain.usecases.LoginUseCase
import com.dezdeqness.presentation.event.AuthorizationSuccess
import com.dezdeqness.presentation.event.CloseAuthorization
import com.dezdeqness.utils.NetworkUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.regex.Pattern
import javax.inject.Inject
import javax.inject.Named

class AuthorizationViewModel @Inject constructor(
    @Named("isLogin") private val isLogin: Boolean,
    private val loginUseCase: LoginUseCase,
    private val accountRepository: AccountRepository,
    networkUtils: NetworkUtils,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    appLogger: AppLogger,
) : BaseViewModel(
    coroutineDispatcherProvider = coroutineDispatcherProvider,
    appLogger = appLogger,
) {

    private val _authorizationStateFlow: MutableStateFlow<AuthorizationState> =
        MutableStateFlow(AuthorizationState())
    val authorizationStateFlow: StateFlow<AuthorizationState> get() = _authorizationStateFlow

    init {
        _authorizationStateFlow.value = _authorizationStateFlow.value.copy(
            isLoading = true,
        )

        if (networkUtils.isInternetAvailable()) {
            val url = if (isLogin) {
                SHIKIMORI_SIGN_IN_URL
            } else {
                SHIKIMORI_SIGN_UP_URL
            }
            _authorizationStateFlow.value = AuthorizationState(url = url)

        } else {
            onEventReceive(CloseAuthorization)
        }
    }

    override val viewModelTag = "AuthorizationViewModel"

    fun onPageStarted() {
        accountRepository
            .getAuthorizationCodeUrl()
            .onSuccess { url ->
                _authorizationStateFlow.value = _authorizationStateFlow.value.copy(
                    url = url,
                    isLoading = false,
                )
            }
    }


    fun onShouldOverrideUrlLoading(url: String) {
        val matcher = Pattern.compile(SHIKIMORI_PATTERN).matcher(url)
        if (matcher.find()) {
            val authCode =
                if (matcher.group().isNullOrEmpty()) ""
                else url
                    .substring(url.lastIndexOf("/"))
                    .replaceFirst("/", "")

            if (authCode.isEmpty()) {
                // TODO: error prompt
                logInfo("Code is empty")
                return
            }

            if (_authorizationStateFlow.value.isLoading) {
                return
            }

            _authorizationStateFlow.value = _authorizationStateFlow.value.copy(
                isLoading = true,
            )

            launchOnIo {
                loginUseCase
                    .invoke(authCode)
                    .onSuccess {
                        val value = _authorizationStateFlow.value
                        _authorizationStateFlow.value =
                            value.copy(isLoading = true)
                        onEventReceive(AuthorizationSuccess)
                    }
                    .onFailure {
                        logInfo("Sign in/Sign up is failed", it)
                    }
            }
        }
    }

    private companion object {
        private const val SHIKIMORI_SIGN_UP_URL = "https://shikimori.one/users/sign_up"
        private const val SHIKIMORI_SIGN_IN_URL = "https://shikimori.one/users/sign_in"

        private const val SHIKIMORI_PATTERN =
            "https?:\\/\\/(?:www\\.)?shikimori\\.one\\/oauth\\/authorize\\/(?:.*)"
    }

}
