package com.example.baseproject.util.ext

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun String.capitalize(): String {
    return replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}

fun String.formatDate(
    inputFormat: String? = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSSZZZZZ",
    outputFormat: String? = "dd MMM yyyy HH:mm"
): String {
    try {
        val inputDateFormatter = SimpleDateFormat(inputFormat, Locale.getDefault())
        val outputDateFormatter = SimpleDateFormat(outputFormat, Locale.getDefault())
        val parsedDate = inputDateFormatter.parse(this)

        return if (parsedDate != null) {
            outputDateFormatter.format(parsedDate)
        } else {
            ""
        }
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return "-"
}

fun String.toTimeZone(): String {
    return when (this) {
        "07:00" -> "WIB"
        "08:00" -> "WITA"
        "09:00" -> "WIT"
        else -> ""
    }
}