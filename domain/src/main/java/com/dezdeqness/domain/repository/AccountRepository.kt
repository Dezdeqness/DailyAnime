package com.dezdeqness.domain.repository

import com.dezdeqness.domain.model.AccountEntity
import com.dezdeqness.domain.model.AuthorizationState
import com.dezdeqness.domain.model.HistoryEntity
import com.dezdeqness.domain.model.TokenEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface AccountRepository {

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

    fun getProfileRemote(): Result<AccountEntity>

    fun getProfileDetails(): Flow<Result<AccountEntity>>

    fun getProfileLocal(): AccountEntity?

    fun getUserHistory(page: Int, limit: Int): Result<List<HistoryEntity>>

    fun saveProfileLocal(accountEntity: AccountEntity)

    fun deleteAccountLocal()

    fun clearUserCookie()

    suspend fun emitAuthorizationState(state: AuthorizationState)

}
