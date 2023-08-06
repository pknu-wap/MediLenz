package com.android.mediproject.core.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun String.toLocalDate(format: String): LocalDate? = if (isEmpty()) null else LocalDate.parse(this, DateTimeFormatter.ofPattern(format))

fun String.toLocalDateTime(format: String): LocalDateTime = LocalDateTime.parse(this, DateTimeFormatter.ofPattern(format))