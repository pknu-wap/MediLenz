package com.android.mediproject.core.model

import android.os.Parcelable
import com.android.mediproject.core.model.common.UiValue
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun String?.toLocalDate(format: String): DateTimeValue = DateTimeValue(this, format)

fun String.toLocalDateTime(format: String): LocalDateTime = LocalDateTime.parse(this, DateTimeFormatter.ofPattern(format))

class DateTimeValue(dateTime: String?, format: String) : UiValue<LocalDate> {
    override val value: LocalDate = dateTime?.run { LocalDate.parse(this, DateTimeFormatter.ofPattern(format)) } ?: LocalDate.MIN
    override val isEmpty: Boolean = value.isEqual(LocalDate.MIN)
}
