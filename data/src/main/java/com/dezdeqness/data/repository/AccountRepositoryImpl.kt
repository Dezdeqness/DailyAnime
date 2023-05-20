package com.dezdeqness.data.repository

import com.dezdeqness.data.datasource.AccountRemoteDataSource
import com.dezdeqness.data.datasource.db.AccountLocalDataSource
import com.dezdeqness.data.manager.TokenManager
import com.dezdeqness.domain.model.AccountEntity
import com.dezdeqness.domain.model.AuthorizationState
import com.dezdeqness.domain.model.HistoryEntity
import com.dezdeqness.domain.model.TokenEntity
import com.dezdeqness.domain.repository.AccountRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val accountRemoteDataSource: AccountRemoteDataSource,
    private val accountLocalDataSource: AccountLocalDataSource,
    private val tokenManager: TokenManager,
) : AccountRepository {

    private val _authorizationState: MutableSharedFlow<AuthorizationState> = MutableSharedFlow()
    override fun authorizationState(): SharedFlow<AuthorizationState> = _authorizationState
    override fun getAuthorizationCodeUrl() = accountRemoteDataSource.getAuthorizationCodeUrl()

    override fun isAuthorized() = accountLocalDataSource.getAccount() != null

    override fun saveToken(tokenEntity: TokenEntity): Result<Boolean> {
        tokenManager.setTokenData(tokenEntity)

        return Result.success(true)
    }

    override fun getToken() = tokenManager.getTokenData()

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

    override fun getProfileDetails() = flow {
        val tokenData = tokenManager.getTokenData()
        if (tokenData.accessToken.isEmpty()) {
            emit(Result.failure(Exception()))
            return@flow
        }

        val profile = getProfileLocal()
        if (profile == null) {
            emit(Result.failure(Exception()))
            return@flow
        }

        emit(Result.success(profile))

        val detailsAccountInfo = accountRemoteDataSource.getDetailsAccountInfo(
            token = tokenData.accessToken,
            userId = profile.id,
        ).onSuccess {
            saveProfileLocal(it)
        }

        emit(detailsAccountInfo)
    }

    override fun getProfileLocal() = accountLocalDataSource.getAccount()

    override fun getUserHistory(page: Int, limit: Int): Result<List<HistoryEntity>> {
        val userId = getProfileLocal()?.id ?: return Result.failure(Throwable())

        return accountRemoteDataSource.getHistory(
            userId = userId,
            page = page,
            limit = limit,
        )
    }

    override fun saveProfileLocal(accountEntity: AccountEntity) {
        accountLocalDataSource.saveAccount(accountEntity)
    }

    override suspend fun emitAuthorizationState(state: AuthorizationState) {
        _authorizationState.emit(state)
    }
}
