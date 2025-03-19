package com.dezdeqness.data.serializer

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.dezdeqness.data.TokenEntityProto
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.aead.AeadKeyTemplates
import com.google.crypto.tink.shaded.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object TokenSerializer : Serializer<TokenEntityProto> {
    private val aead: Aead

    init {
        AeadConfig.register()
        val keysetHandle = KeysetHandle.generateNew(AeadKeyTemplates.AES256_GCM)
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
        }
    }

    override suspend fun writeTo(t: TokenEntityProto, output: OutputStream) {
        val encryptedBytes = aead.encrypt(t.toByteArray(), null)
        output.write(encryptedBytes)
    }
}
