package com.dezdeqness.contract.auth.repository

import com.dezdeqness.contract.auth.model.AuthorizationState
import com.dezdeqness.contract.auth.model.TokenEntity
import kotlinx.coroutines.flow.SharedFlow

interface AuthRepository {
    fun authorizationState(): SharedFlow<AuthorizationState>

    fun getAuthorizationCodeUrl(): Result<String>

    fun isAuthorized(): Boolean

    fun saveToken(tokenEntity: TokenEntity): Result<Boolean>

    fun getToken(): TokenEntity

    fun isSessionExpired(): Boolean

    fun clearToken()

    fun login(code: String): Result<TokenEntity>

    fun refresh(): Result<TokenEntity>

    fun logout(): Result<Boolean>

    suspend fun emitAuthorizationState(state: AuthorizationState)
}
