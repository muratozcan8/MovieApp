package com.obss.firstapp.utils.ext

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

fun String.formatToReadableDate(): String {
    val inputFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    inputFormatter.timeZone = TimeZone.getTimeZone("UTC")

    val birthDate = inputFormatter.parse(this)

    val outputFormatter = SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH)
    val formattedDate = birthDate?.let { outputFormatter.format(it) }

    return "$formattedDate"
}

fun String.formatAndCalculateAge(): String {
    val inputFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val birthDate = inputFormatter.parse(this)

    val outputFormatter = SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH)
    val formattedDate = birthDate?.let { outputFormatter.format(it) }

    val birthCalendar = Calendar.getInstance()
    if (birthDate != null) {
        birthCalendar.time = birthDate
    }
    val today = Calendar.getInstance()
    var age = today.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR)

    if (today.get(Calendar.DAY_OF_YEAR) < birthCalendar.get(Calendar.DAY_OF_YEAR)) {
        age--
    }
    return "$formattedDate ($age)"
}
