package com.android.mediproject.core.model

import com.android.mediproject.core.model.common.UiValue
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun String.toLocalDate(format: String): DateTimeValue = DateTimeValue(this, format)

fun String.toLocalDateTime(format: String): LocalDateTime = LocalDateTime.parse(this, DateTimeFormatter.ofPattern(format))

class DateTimeValue(dateTime: String, format: String) : UiValue<LocalDate> {
    override val value: LocalDate =
        if (dateTime.isNotEmpty()) LocalDate.parse(dateTime, DateTimeFormatter.ofPattern(format)) else LocalDate.MIN
    override val isEmpty: Boolean = value.isEqual(LocalDate.MIN)
}
