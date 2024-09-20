package kr.musekee.faremeter.libs

import android.icu.text.DecimalFormat

fun Int.wonFormat(): String {
    val formatter = DecimalFormat("###,###")
    return formatter.format(this)
}