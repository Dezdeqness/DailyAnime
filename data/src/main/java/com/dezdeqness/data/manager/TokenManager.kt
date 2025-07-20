package com.dezdeqness.data.manager

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.dezdeqness.data.TokenEntityProto
import com.dezdeqness.data.serializer.TokenSerializer
import com.dezdeqness.contract.auth.model.TokenEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


class TokenManager @Inject constructor(private val context: Context) {

    private val Context.tokenDataStore: DataStore<TokenEntityProto> by dataStore(
        fileName = "token_preferences.pb",
        serializer = TokenSerializer(context)
    )

    fun setTokenData(tokenEntity: TokenEntity) {
        val proto = TokenEntityProto.newBuilder()
            .setAccessToken(tokenEntity.accessToken)
            .setRefreshToken(tokenEntity.refreshToken)
            .setCreatedIn(tokenEntity.createdIn)
            .setExpiresIn(tokenEntity.expiresIn)
            .build()

        runBlocking {
            context.tokenDataStore.updateData { _ -> proto }
        }
    }

    fun isTokenExpired(): Boolean {
        val tokenData = getTokenData()
        val currentTime = System.currentTimeMillis() / DIVIDE_VALUE
        return currentTime >= tokenData.createdIn + tokenData.expiresIn + TIME_SHIFT
    }

    fun getTokenData(): TokenEntity {
        val protoData = runBlocking { context.tokenDataStore.data.first() }
        return TokenEntity(
            accessToken = protoData.accessToken,
            refreshToken = protoData.refreshToken,
            createdIn = protoData.createdIn,
            expiresIn = protoData.expiresIn
        )
    }

    fun clear() {
        runBlocking {
            context.tokenDataStore.updateData { _ ->
                TokenEntityProto.getDefaultInstance()
            }
        }
    }

    companion object {
        private const val TIME_SHIFT = 60
        private const val DIVIDE_VALUE = 1000
    }

}
