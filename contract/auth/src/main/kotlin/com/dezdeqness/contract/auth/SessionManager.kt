package com.dezdeqness.contract.auth

import com.dezdeqness.contract.auth.model.SessionState
import kotlinx.coroutines.flow.StateFlow

interface SessionManager {
    val sessionState: StateFlow<SessionState>
    val currentSession: SessionState.Authenticated? get() = sessionState.value as? SessionState.Authenticated
    val isAuthorized: Boolean

    suspend fun login(code: String): Result<Unit>
    suspend fun logout(): Result<Unit>
    suspend fun restoreSession()

    suspend fun getValidToken(): Result<String>
}
