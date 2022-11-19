package com.dezdeqness.data.repository

import com.dezdeqness.data.datasource.AccountRemoteDataSource
import com.dezdeqness.data.datasource.db.AccountLocalDataSource
import com.dezdeqness.data.manager.TokenManager
import com.dezdeqness.domain.model.AccountEntity
import com.dezdeqness.domain.model.TokenEntity
import com.dezdeqness.domain.repository.AccountRepository
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val accountRemoteDataSource: AccountRemoteDataSource,
    private val accountLocalDataSource: AccountLocalDataSource,
    private val tokenManager: TokenManager,
) : AccountRepository {

    override fun getAuthorizationCodeUrl() = accountRemoteDataSource.getAuthorizationCodeUrl()

    override fun isAuthorized() = accountLocalDataSource.getAccount() != null

    override fun saveToken(tokenEntity: TokenEntity): Result<Boolean> {
        tokenManager.setTokenData(tokenEntity)

        return Result.success(true)
    }

    override fun isSessionExpired() = tokenManager.isTokenExpired()

    override fun clearToken() {
        tokenManager.clear()
    }

    override fun login(code: String) = accountRemoteDataSource.login(code)

    override fun refresh(): Result<TokenEntity> {
        val tokenData = tokenManager.getTokenData()
        return accountRemoteDataSource.refresh(tokenData.refreshToken)
    }

    override fun logout(): Result<String> {
        TODO("Not yet implemented")
    }

    override fun getProfileRemote(): Result<AccountEntity> {
        val tokenData = tokenManager.getTokenData()
        return accountRemoteDataSource.getBriefAccountInfo(tokenData.accessToken)
    }

    override fun getProfileLocal() = accountLocalDataSource.getAccount()

    override fun saveProfileLocal(accountEntity: AccountEntity) {
        accountLocalDataSource.saveAccount(accountEntity)
    }

}
