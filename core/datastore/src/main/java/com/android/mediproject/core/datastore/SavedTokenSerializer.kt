package com.android.mediproject.core.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject


class SavedTokenSerializer @Inject constructor() : Serializer<SavedToken> {

    override val defaultValue: SavedToken
        get() = SavedToken.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): SavedToken {
        try {
            return SavedToken.parseFrom(input)
        } catch (exception: Exception) {
            throw CorruptionException("Failed to read proto.", exception)
        }
    }

    override suspend fun writeTo(t: SavedToken, output: OutputStream) {
        t.writeTo(output)
    }

}
