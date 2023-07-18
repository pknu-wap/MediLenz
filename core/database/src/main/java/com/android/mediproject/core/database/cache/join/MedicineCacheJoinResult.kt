package com.android.mediproject.core.database.cache.join

import androidx.room.ColumnInfo
import androidx.room.RoomWarnings

@SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
data class MedicineCacheJoinResult(
    @ColumnInfo(name = "item_seq")
    val itemSeq: String,
    @ColumnInfo(name = "data") val data: ByteArray,
    @ColumnInfo(name = "image_url") val imageUrl: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MedicineCacheJoinResult

        if (itemSeq != other.itemSeq) return false
        if (!data.contentEquals(other.data)) return false
        if (imageUrl != other.imageUrl) return false

        return true
    }

    override fun hashCode(): Int {
        var result = itemSeq.hashCode()
        result = 31 * result + data.contentHashCode()
        result = 31 * result + imageUrl.hashCode()
        return result
    }
}
