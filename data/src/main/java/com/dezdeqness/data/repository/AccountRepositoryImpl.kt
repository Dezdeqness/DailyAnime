package com.dezdeqness.data.repository

import com.apollographql.apollo.ApolloClient
import com.dezdeqness.data.HomeQuery
import com.dezdeqness.data.core.CookieCleaner
import com.dezdeqness.data.datasource.AccountRemoteDataSource
import com.dezdeqness.data.datasource.db.AccountLocalDataSource
import com.dezdeqness.data.manager.TokenManager
import com.dezdeqness.data.type.OrderEnum
import com.dezdeqness.data.type.PositiveInt
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
    private val apolloClient: ApolloClient,
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

    override suspend fun test() {
        val result = apolloClient.query(HomeQuery(
            genre1 = "2",
            genre2 = "30",
            genre3 = "140",
            limit = 5,
            order = OrderEnum.popularity,
            )).execute()
        result
    }

    override suspend fun emitAuthorizationState(state: AuthorizationState) {
        _authorizationState.emit(state)
    }
}
