package com.dezdeqness.di.source.unsupported

import com.dezdeqness.contract.auth.model.TokenEntity
import com.dezdeqness.contract.auth.repository.AuthRepository
import javax.inject.Inject

class UnsupportedAuthRepository @Inject constructor() : AuthRepository {

    override fun getAuthorizationCodeUrl(): Result<String> = unsupported()

    override fun isAuthorized() = false

    override fun saveToken(tokenEntity: TokenEntity): Result<Boolean> = unsupported()

    override fun getToken() = TokenEntity(accessToken = "", refreshToken = "", createdIn = 0, expiresIn = 0)

    override fun isSessionExpired() = false

    override fun clearToken() = Unit

    override fun login(code: String): Result<TokenEntity> = unsupported()

    override fun refresh(): Result<TokenEntity> = unsupported()

    override fun logout(): Result<Boolean> = unsupported()
}
