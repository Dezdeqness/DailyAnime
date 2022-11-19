package com.dezdeqness.domain.repository

import com.dezdeqness.domain.model.AccountEntity
import com.dezdeqness.domain.model.TokenEntity

interface AccountRepository {

    fun getAuthorizationCodeUrl(): Result<String>

    fun isAuthorized(): Boolean

    fun saveToken(tokenEntity: TokenEntity): Result<Boolean>

    fun isSessionExpired(): Boolean

    fun clearToken()

    fun login(code: String): Result<TokenEntity>

    fun refresh(): Result<TokenEntity>

    fun logout(): Result<String>

    fun getProfileRemote(): Result<AccountEntity>

    fun getProfileLocal(): AccountEntity?

    fun saveProfileLocal(accountEntity: AccountEntity)

}
