package com.dezdeqness.data.manager

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.security.crypto.MasterKey.DEFAULT_AES_GCM_MASTER_KEY_SIZE
import androidx.security.crypto.MasterKey.DEFAULT_MASTER_KEY_ALIAS
import com.dezdeqness.data.TokenEntityProto
import com.dezdeqness.data.serializer.TokenSerializer
import com.dezdeqness.domain.model.TokenEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import androidx.core.content.edit


class TokenManager @Inject constructor(private val context: Context) {

    private var sharedPreferences: SharedPreferences

    init {
        val spec = KeyGenParameterSpec.Builder(
            DEFAULT_MASTER_KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(DEFAULT_AES_GCM_MASTER_KEY_SIZE)
            .build()

        val masterKey = MasterKey.Builder(context)
            .setKeyGenParameterSpec(spec)
            .build()

        sharedPreferences = EncryptedSharedPreferences.create(
            context,
            FILENAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private val Context.tokenDataStore: DataStore<TokenEntityProto> by dataStore(
        fileName = "token_preferences.pb",
        serializer = TokenSerializer
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

    fun migrateToProtoStore() {
        val accessToken = sharedPreferences.getString(KEY_ACCESS_TOKEN, null)
        val refreshToken = sharedPreferences.getString(KEY_REFRESH_TOKEN, null)
        val createdAt = sharedPreferences.getLong(KEY_CREATED_AT, 0)
        val expiresIn = sharedPreferences.getLong(KEY_EXPIRES_IN, 0)

        if (accessToken != null && refreshToken != null) {
            setTokenData(
                TokenEntity(
                    accessToken = accessToken,
                    refreshToken = refreshToken,
                    createdIn = createdAt,
                    expiresIn = expiresIn
                )
            )
            sharedPreferences.edit { clear() }
        }
    }


    companion object {
        private const val FILENAME = "token_preferences"
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_CREATED_AT = "created_at"
        private const val KEY_EXPIRES_IN = "expires_in"

        private const val TIME_SHIFT = 60
        private const val DIVIDE_VALUE = 1000
    }

}
