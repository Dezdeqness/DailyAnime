package com.dezdeqness.data.repository

import android.webkit.CookieManager
import com.dezdeqness.data.core.CookieCleaner
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
    private val cookieCleaner: CookieCleaner,
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

    override fun logout() = accountRemoteDataSource.logout()

    override fun getProfileRemote(): Result<AccountEntity> {
        return accountRemoteDataSource.getBriefAccountInfo()
    }

    override fun getProfileDetails() = flow {
        val profile = getProfileLocal()
        if (profile == null) {
            emit(Result.failure(Exception("Profile failure")))
            return@flow
        }

        emit(Result.success(profile))

        val detailsAccountInfo = accountRemoteDataSource.getDetailsAccountInfo(
            userId = profile.id,
        ).onSuccess {
            saveProfileLocal(it)
        }

        emit(detailsAccountInfo)
    }

    override fun getProfileLocal() = accountLocalDataSource.getAccount()

    override fun getUserHistory(page: Int, limit: Int): Result<List<HistoryEntity>> {
        val userId =
            getProfileLocal()?.id ?: return Result.failure(Throwable("Local profile failure"))

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
