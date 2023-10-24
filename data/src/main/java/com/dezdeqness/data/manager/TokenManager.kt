package com.dezdeqness.data.manager

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.security.crypto.MasterKey.DEFAULT_AES_GCM_MASTER_KEY_SIZE
import androidx.security.crypto.MasterKey.DEFAULT_MASTER_KEY_ALIAS
import com.dezdeqness.domain.model.TokenEntity
import javax.inject.Inject


class TokenManager @Inject constructor(context: Context) {

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

    fun setTokenData(tokenEntity: TokenEntity) {
        val editor = sharedPreferences.edit()

        editor.putString(KEY_ACCESS_TOKEN, tokenEntity.accessToken)
        editor.putString(KEY_REFRESH_TOKEN, tokenEntity.refreshToken)
        editor.putLong(KEY_CREATED_AT, tokenEntity.createdIn)
        editor.putLong(KEY_EXPIRES_IN, tokenEntity.expiresIn)

        editor.apply()
    }

    fun isTokenExpired(): Boolean {
        val tokenData = getTokenData()
        val currentTime = System.currentTimeMillis() / DIVIDE_VALUE
        return currentTime >= tokenData.createdIn + tokenData.expiresIn + TIME_SHIFT
    }

    fun getTokenData(): TokenEntity {
        val accessToken = sharedPreferences.getString(KEY_ACCESS_TOKEN, "").orEmpty()
        val refreshToken = sharedPreferences.getString(KEY_REFRESH_TOKEN, "").orEmpty()
        val createdIn = sharedPreferences.getLong(KEY_CREATED_AT, 0)
        val expiresIn = sharedPreferences.getLong(KEY_EXPIRES_IN, 0)
        return TokenEntity(
            accessToken = accessToken,
            refreshToken = refreshToken,
            createdIn = createdIn,
            expiresIn = expiresIn,
        )
    }

    fun clear() {
        sharedPreferences.edit().clear().apply()
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
