package com.fjr619.studyfocus.presentation.util

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun Long?.changeMillisToDateString(): String {
    val date: LocalDate = this?.let {
        Instant
            .ofEpochMilli(it)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    } ?: LocalDate.now()
    return date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
}

fun Instant.withoutTime() = atZone(ZoneOffset.UTC).withHour(0)
    .withMinute(0).withSecond(0).withNano(0).toInstant()

fun Long.toHours(): Float {
    val hours = this.toFloat() / 3600f
    return "%.2f".format(hours).toFloat()
}

fun Int.pad(): String {
    return this.toString().padStart(length = 2, padChar = '0')
}