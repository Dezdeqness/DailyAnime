package com.dezdeqness.data.serializer

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.dezdeqness.data.TokenEntityProto
import com.google.crypto.tink.Aead
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.aead.AeadKeyTemplates
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import com.google.crypto.tink.shaded.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class TokenSerializer @Inject constructor(context: Context) : Serializer<TokenEntityProto> {
    private var aead: Aead

    init {
        AeadConfig.register()

        val keysetHandle = AndroidKeysetManager.Builder()
            .withSharedPref(context, KEYSET_NAME, PREFERENCE_FILE_NAME)
            .withKeyTemplate(AeadKeyTemplates.AES256_GCM)
            .withMasterKeyUri(MASTER_KEY_URI)
            .build()
            .keysetHandle

        aead = keysetHandle.getPrimitive(Aead::class.java)
    }

    override val defaultValue: TokenEntityProto = TokenEntityProto.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): TokenEntityProto {
        return try {
            val encryptedBytes = input.readBytes()
            val decryptedBytes = aead.decrypt(encryptedBytes, null)
            TokenEntityProto.parseFrom(decryptedBytes)
        } catch (e: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read TokenEntityProto", e)
        } catch (e: Exception) {
            throw CorruptionException("Decryption failed", e)
        }
    }

    override suspend fun writeTo(t: TokenEntityProto, output: OutputStream) {
        val encryptedBytes = aead.encrypt(t.toByteArray(), null)
        output.write(encryptedBytes)
    }

    companion object {
        private const val KEYSET_NAME = "master_keyset"
        private const val PREFERENCE_FILE_NAME = "secure_prefs"
        private const val MASTER_KEY_URI = "android-keystore://master_key"
    }
}
