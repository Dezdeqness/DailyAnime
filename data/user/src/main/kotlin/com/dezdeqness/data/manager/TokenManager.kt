package com.dezdeqness.data.manager

import androidx.datastore.core.DataStore
import com.dezdeqness.contract.auth.model.TokenEntity
import com.dezdeqness.data.TokenEntityProto
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class TokenManager(private val tokenDataStore: DataStore<TokenEntityProto>) {

    fun setTokenData(tokenEntity: TokenEntity) {
        val proto = TokenEntityProto.newBuilder()
            .setAccessToken(tokenEntity.accessToken)
            .setRefreshToken(tokenEntity.refreshToken)
            .setCreatedIn(tokenEntity.createdIn)
            .setExpiresIn(tokenEntity.expiresIn)
            .build()

        runBlocking {
            tokenDataStore.updateData { _ -> proto }
        }
    }

    fun isTokenExpired(): Boolean {
        val tokenData = getTokenData()
        val currentTime = System.currentTimeMillis() / DIVIDE_VALUE
        return currentTime >= tokenData.createdIn + tokenData.expiresIn + TIME_SHIFT
    }

    fun getTokenData(): TokenEntity {
        val protoData = runBlocking { tokenDataStore.data.first() }
        return TokenEntity(
            accessToken = protoData.accessToken,
            refreshToken = protoData.refreshToken,
            createdIn = protoData.createdIn,
            expiresIn = protoData.expiresIn,
        )
    }

    fun clear() {
        runBlocking {
            tokenDataStore.updateData { _ ->
                TokenEntityProto.getDefaultInstance()
            }
        }
    }

    companion object {
        private const val TIME_SHIFT = 60
        private const val DIVIDE_VALUE = 1000
    }
}
