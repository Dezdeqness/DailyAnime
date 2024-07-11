package com.dezdeqness.data.datasource

import com.dezdeqness.data.AccountApiService
import com.dezdeqness.data.AuthorizationApiService
import com.dezdeqness.data.core.BaseDataSource
import com.dezdeqness.data.core.createApiException
import com.dezdeqness.data.mapper.AccountMapper
import com.dezdeqness.domain.model.TokenEntity
import okhttp3.HttpUrl
import javax.inject.Inject

class AccountRemoteDataSourceImpl @Inject constructor(
    private val accountApiService: AccountApiService,
    private val authorizationApiService: AuthorizationApiService,
    private val accountMapper: AccountMapper,
) : AccountRemoteDataSource, BaseDataSource() {

    override fun getAuthorizationCodeUrl(): Result<String> {
        val url = HttpUrl.Builder().scheme("https").host("shikimori.one").addPathSegment("oauth")
            .addPathSegment("authorize").addQueryParameter("client_id", CLIENT_ID)
            .addQueryParameter("redirect_uri", REDIRECT_URI)
            .addQueryParameter("response_type", RESPONSE_TYPE).addQueryParameter("scope", SCOPE)
            .build()

        return Result.success(url.toString())
    }

    override fun login(code: String) = tryWithCatch {
        val response = authorizationApiService.login(
            code = code,
            secret = CLIENT_CODE,
            id = CLIENT_ID,
            uri = REDIRECT_URI,
        ).execute()

        val responseBody = response.body()
        if (response.isSuccessful && responseBody != null) {
            Result.success(
                TokenEntity(
                    accessToken = responseBody.accessToken,
                    refreshToken = responseBody.refreshToken,
                    createdIn = responseBody.createdAt,
                    expiresIn = responseBody.expiresIn,
                )
            )
        } else {
            throw response.createApiException()
        }
    }

    override fun logout() = tryWithCatch {
        val response = accountApiService.logout().execute()

        val responseBody = response.body()
        if (response.isSuccessful && responseBody != null) {
            Result.success(responseBody)
        } else {
            throw response.createApiException()
        }
    }

    override fun getBriefAccountInfo() = tryWithCatch {
        val response = accountApiService.getProfile().execute()

        val responseBody = response.body()
        if (response.isSuccessful && responseBody != null) {
            Result.success(
                accountMapper.fromResponse(responseBody)
            )
        } else {
            throw response.createApiException()
        }
    }

    override fun getDetailsAccountInfo(userId: Long) = tryWithCatch {
        val response = accountApiService.getProfileDetails(id = userId).execute()

        val responseBody = response.body()
        if (response.isSuccessful && responseBody != null) {
            Result.success(
                accountMapper.fromResponse(responseBody)
            )
        } else {
            throw response.createApiException()
        }
    }

    override fun getHistory(userId: Long, page: Int, limit: Int) = tryWithCatch {
        val response = accountApiService.getUserHistory(
            id = userId,
            page = page,
            limit = limit
        ).execute()

        val responseBody = response.body()
        if (response.isSuccessful && responseBody != null) {
            Result.success(
                responseBody.map(accountMapper::fromResponse)
            )
        } else {
            throw response.createApiException()
        }
    }

    override fun refresh(token: String) = tryWithCatch {
        val response = authorizationApiService.refresh(
            token = token,
            secret = CLIENT_CODE,
            id = CLIENT_ID,
            uri = REDIRECT_URI,
        ).execute()

        val responseBody = response.body()
        if (response.isSuccessful && responseBody != null) {
            Result.success(
                TokenEntity(
                    accessToken = responseBody.accessToken,
                    refreshToken = responseBody.refreshToken,
                    createdIn = responseBody.createdAt,
                    expiresIn = responseBody.expiresIn,
                )
            )
        } else {
            throw response.createApiException()
        }
    }

    companion object {
        private const val CLIENT_ID = "9Hx5uIoQ_dBr2VTVGo5L2EH6FrizLkYgsO_Y-0CAyQk"
        private const val CLIENT_CODE = "mnEmuKPi5l5SIL2kwYcac0NcrHhH8FhW8TS0oHwEvT0"
        private const val REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob"
        private const val RESPONSE_TYPE = "code"
        private const val SCOPE = "user_rates"
    }
}
