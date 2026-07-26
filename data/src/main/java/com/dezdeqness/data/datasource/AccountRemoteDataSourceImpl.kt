package com.dezdeqness.data.datasource

import com.dezdeqness.contract.auth.model.TokenEntity
import com.dezdeqness.data.AccountApiService
import com.dezdeqness.data.AuthorizationApiService
import com.dezdeqness.data.core.BaseDataSource
import com.dezdeqness.data.core.config.AuthConfig
import com.dezdeqness.data.core.config.ConfigManager
import com.dezdeqness.data.core.createApiException
import com.dezdeqness.data.mapper.AccountMapper
import dagger.Lazy
import javax.inject.Inject
import okhttp3.HttpUrl.Companion.toHttpUrl

class AccountRemoteDataSourceImpl @Inject constructor(
    private val accountApiService: Lazy<AccountApiService>,
    private val authorizationApiService: Lazy<AuthorizationApiService>,
    private val accountMapper: AccountMapper,
    private val configManager: ConfigManager,
    private val authConfig: AuthConfig,
) : AccountRemoteDataSource, BaseDataSource() {

    override fun getAuthorizationCodeUrl(): Result<String> {
        val url = configManager.baseUrl.toHttpUrl()
            .newBuilder()
            .addPathSegment("oauth")
            .addPathSegment("authorize")
            .addQueryParameter("client_id", authConfig.clientId)
            .addQueryParameter("redirect_uri", authConfig.redirectUri)
            .addQueryParameter("response_type", RESPONSE_TYPE)
            .addQueryParameter("scope", SCOPE)
            .build()

        return Result.success(url.toString())
    }

    override fun login(code: String) = tryWithCatch {
        val response = authorizationApiService.get().login(
            code = code,
            secret = authConfig.clientSecret,
            id = authConfig.clientId,
            uri = authConfig.redirectUri,
        ).execute()

        val responseBody = response.body()
        if (response.isSuccessful && responseBody != null) {
            Result.success(
                TokenEntity(
                    accessToken = responseBody.accessToken,
                    refreshToken = responseBody.refreshToken,
                    createdIn = responseBody.createdAt,
                    expiresIn = responseBody.expiresIn,
                ),
            )
        } else {
            throw response.createApiException()
        }
    }

    override fun logout() = tryWithCatch {
        val response = accountApiService.get().logout().execute()

        val responseBody = response.body()
        if (response.isSuccessful && responseBody != null) {
            Result.success(true)
        } else {
            throw response.createApiException()
        }
    }

    override fun getBriefAccountInfo() = tryWithCatch {
        val response = accountApiService.get().getProfile().execute()

        val responseBody = response.body()
        if (response.isSuccessful && responseBody != null) {
            Result.success(
                accountMapper.fromResponse(responseBody),
            )
        } else {
            throw response.createApiException()
        }
    }

    override fun getDetailsAccountInfo(userId: Long) = tryWithCatch {
        val response = accountApiService.get().getProfileDetails(id = userId).execute()

        val responseBody = response.body()
        if (response.isSuccessful && responseBody != null) {
            Result.success(
                accountMapper.fromResponse(responseBody),
            )
        } else {
            throw response.createApiException()
        }
    }

    override fun getHistory(userId: Long, page: Int, limit: Int) = tryWithCatch {
        val response = accountApiService.get().getUserHistory(
            id = userId,
            page = page,
            limit = limit,
        ).execute()

        val responseBody = response.body()
        if (response.isSuccessful && responseBody != null) {
            Result.success(
                responseBody.map(accountMapper::fromResponse),
            )
        } else {
            throw response.createApiException()
        }
    }

    override fun refresh(token: String) = tryWithCatch {
        val response = authorizationApiService.get().refresh(
            token = token,
            secret = authConfig.clientSecret,
            id = authConfig.clientId,
            uri = authConfig.redirectUri,
        ).execute()

        val responseBody = response.body()
        if (response.isSuccessful && responseBody != null) {
            Result.success(
                TokenEntity(
                    accessToken = responseBody.accessToken,
                    refreshToken = responseBody.refreshToken,
                    createdIn = responseBody.createdAt,
                    expiresIn = responseBody.expiresIn,
                ),
            )
        } else {
            throw response.createApiException()
        }
    }

    companion object {
        private const val RESPONSE_TYPE = "code"
        private const val SCOPE = "user_rates"
    }
}
