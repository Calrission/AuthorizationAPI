package com.cit.common

import java.time.*
import java.time.format.DateTimeFormatter

const val timePattern = "HH:mm:ss"
const val datePattern = "dd.MM.yy"
const val fullPattern = "$datePattern $timePattern"

fun LocalDate.toPatternString(pattern: String = datePattern): String{
    return this.format(DateTimeFormatter.ofPattern(pattern))
}

fun LocalTime.toPatternString(pattern: String = timePattern): String{
    return this.format(DateTimeFormatter.ofPattern(pattern))
}

fun String.toPatternDate(pattern: String = datePattern): LocalDate{
    return LocalDate.parse(this, DateTimeFormatter.ofPattern(pattern))
}

fun String.toPatternTime(pattern: String = timePattern): LocalTime{
    return LocalTime.parse(this, DateTimeFormatter.ofPattern(pattern))
}