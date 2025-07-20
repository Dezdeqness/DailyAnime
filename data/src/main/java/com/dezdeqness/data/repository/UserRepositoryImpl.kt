package com.dezdeqness.data.repository

import com.dezdeqness.contract.history.model.HistoryEntity
import com.dezdeqness.contract.history.repository.HistoryRepository
import com.dezdeqness.data.core.CookieCleaner
import com.dezdeqness.data.datasource.AccountRemoteDataSource
import com.dezdeqness.data.datasource.db.AccountLocalDataSource
import com.dezdeqness.data.exception.UserLocalNotFound
import com.dezdeqness.data.manager.TokenManager
import com.dezdeqness.contract.user.model.AccountEntity
import com.dezdeqness.contract.auth.model.AuthorizationState
import com.dezdeqness.contract.auth.model.TokenEntity
import com.dezdeqness.contract.auth.repository.AuthRepository
import com.dezdeqness.contract.user.repository.UserRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val accountRemoteDataSource: AccountRemoteDataSource,
    private val accountLocalDataSource: AccountLocalDataSource,
    private val tokenManager: TokenManager,
    private val cookieCleaner: CookieCleaner,
) : UserRepository, HistoryRepository, AuthRepository {

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

    override fun logout() = accountRemoteDataSource.logout()

    override fun getProfileRemote(): Result<AccountEntity> {
        return accountRemoteDataSource.getBriefAccountInfo()
    }

    override fun getProfileDetails() = flow {
        val profile = getProfileLocal()
        if (profile == null) {
            emit(Result.failure(UserLocalNotFound()))
            return@flow
        }

        val detailsAccountInfo = accountRemoteDataSource.getDetailsAccountInfo(
            userId = profile.id,
        ).onSuccess {
            saveProfileLocal(it)
        }

        if (detailsAccountInfo.isFailure) {
            emit(Result.success(profile))
        } else {
            emit(detailsAccountInfo)
        }
    }

    override fun getProfileLocal() = accountLocalDataSource.getAccount()

    override fun getUserHistory(page: Int, limit: Int): Result<List<HistoryEntity>> {
        val userId = getProfileLocal()?.id ?: return Result.failure(UserLocalNotFound())

        return accountRemoteDataSource.getHistory(
            userId = userId,
            page = page,
            limit = limit,
        )
    }

    override fun saveProfileLocal(accountEntity: AccountEntity) {
        accountLocalDataSource.saveAccount(accountEntity)
    }

    override fun deleteAccountLocal() {
        accountLocalDataSource.deleteAccount()
    }

    override fun clearUserCookie() {
        cookieCleaner.clear()
    }

    override suspend fun emitAuthorizationState(state: AuthorizationState) {
        _authorizationState.emit(state)
    }
}
