package com.android.mediproject.core.database

import androidx.room.TypeConverter
import java.time.LocalDateTime

object RoomTypeConverter {
    @TypeConverter
    fun fromTimeStamp(timeStamp: String?): LocalDateTime? = timeStamp?.let { LocalDateTime.parse(it) }

    @TypeConverter
    fun toTimeStamp(date: LocalDateTime?): String? = date?.toString()

}