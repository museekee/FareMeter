package kr.museekee.faremeter.libs

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun getDate(milliSeconds: Long, dateFormat: String?): String {
    val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())

    val calendar: Calendar = Calendar.getInstance()
    calendar.setTimeInMillis(milliSeconds)
    return formatter.format(calendar.time)
}