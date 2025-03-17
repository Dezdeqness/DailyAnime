package com.dezdeqness.presentation.features.authorization

import androidx.core.net.toUri
import com.dezdeqness.data.core.AppLogger
import com.dezdeqness.core.BaseViewModel
import com.dezdeqness.core.CoroutineDispatcherProvider
import com.dezdeqness.domain.repository.AccountRepository
import com.dezdeqness.domain.usecases.LoginUseCase
import com.dezdeqness.presentation.event.AuthUrl
import com.dezdeqness.presentation.event.AuthorizationSuccess
import com.dezdeqness.presentation.event.CloseAuthorization
import com.dezdeqness.utils.NetworkUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
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
            accountRepository
                .getAuthorizationCodeUrl()
                .onSuccess { url ->
                    onEventReceive(AuthUrl(url))
                }
                .onFailure {
                    logInfo("Url is incorrect")
                }

        } else {
            onEventReceive(CloseAuthorization)
        }
    }

    override val viewModelTag = "AuthorizationViewModel"

    fun onHandleDeeplink(data: String) {
        val uri = data.toUri()
        val code = uri.getQueryParameter(CODE_KEY)

        if (code.isNullOrEmpty().not()) {
            _authorizationStateFlow.update {
                it.copy(isLoading = true)
            }
            launchOnIo {
                loginUseCase
                    .invoke(code)
                    .onSuccess {
                        onEventReceive(AuthorizationSuccess)
                    }
                    .onFailure {
                        logInfo("Sign in/Sign up is failed", it)
                    }
            }

        }
    }

    private companion object {
        private const val CODE_KEY = "code"
    }

}
