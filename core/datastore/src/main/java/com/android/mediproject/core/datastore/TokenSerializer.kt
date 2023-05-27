package com.android.mediproject.core.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject


class ConnectionTokenSerializer @Inject constructor() : Serializer<ConnectionToken> {
    
    override val defaultValue: ConnectionToken
        get() = ConnectionToken.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): ConnectionToken {
        try {
            @Suppress("BlockingMethodInNonBlockingContext")
            return ConnectionToken.parseFrom(input)
        } catch (exception: Exception) {
            throw CorruptionException("Failed to read proto.", exception)
        }
    }

    override suspend fun writeTo(t: ConnectionToken, output: OutputStream) {
        @Suppress("BlockingMethodInNonBlockingContext")
        t.writeTo(output)
    }

}