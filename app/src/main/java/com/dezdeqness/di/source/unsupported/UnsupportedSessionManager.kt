package com.dezdeqness.di.source.unsupported

import com.dezdeqness.contract.auth.SessionManager
import com.dezdeqness.contract.auth.model.SessionState
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UnsupportedSessionManager @Inject constructor() : SessionManager {

    override val sessionState: StateFlow<SessionState> =
        MutableStateFlow<SessionState>(SessionState.Unauthenticated).asStateFlow()

    override val isAuthorized = false

    override suspend fun login(code: String): Result<Unit> = unsupported()

    override suspend fun logout(): Result<Unit> = unsupported()

    override suspend fun restoreSession() = Unit

    override suspend fun getValidToken(): Result<String> = unsupported()
}
