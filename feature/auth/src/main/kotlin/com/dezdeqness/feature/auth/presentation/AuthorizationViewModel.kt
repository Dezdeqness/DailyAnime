package com.dezdeqness.feature.auth.presentation

import androidx.core.net.toUri
import com.dezdeqness.contract.auth.SessionManager
import com.dezdeqness.contract.auth.repository.AuthRepository
import com.dezdeqness.foundation.BaseViewModel
import com.dezdeqness.foundation.Logger
import com.dezdeqness.foundation.coroutines.CoroutineDispatcherProvider
import com.dezdeqness.foundation.utils.NetworkUtils
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

class AuthorizationViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val authRepository: AuthRepository,
    private val networkUtils: NetworkUtils,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    logger: Logger,
) : BaseViewModel(coroutineDispatcherProvider, logger) {

    override val viewModelTag = "AuthorizationViewModel"

    private val _authorizationStateFlow: MutableStateFlow<AuthorizationState> =
        MutableStateFlow(AuthorizationState())
    val authorizationStateFlow: StateFlow<AuthorizationState> get() = _authorizationStateFlow

    private val effectChannel = Channel<AuthorizationEffect>(Channel.BUFFERED)
    val effects: Flow<AuthorizationEffect> = effectChannel.receiveAsFlow()

    init {
        _authorizationStateFlow.update { it.copy(isLoading = true) }

        if (networkUtils.isInternetAvailable()) {
            authRepository
                .getAuthorizationCodeUrl()
                .onSuccess { url -> effectChannel.trySend(AuthorizationEffect.OpenUrl(url)) }
                .onFailure { logInfo("Url is incorrect") }
        } else {
            effectChannel.trySend(AuthorizationEffect.Close)
        }
    }

    fun onHandleDeeplink(data: String) {
        val uri = data.toUri()
        val code = uri.getQueryParameter(CODE_KEY)

        if (!code.isNullOrEmpty()) {
            _authorizationStateFlow.update { it.copy(isLoading = true) }
            launchOnIo {
                sessionManager
                    .login(code)
                    .onSuccess { effectChannel.trySend(AuthorizationEffect.Success) }
                    .onFailure { logInfo("Sign in/Sign up is failed", it) }
            }
        }
    }

    private companion object {
        private const val CODE_KEY = "code"
    }
}
