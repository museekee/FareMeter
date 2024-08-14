package kr.musekee.faremeter.libs

fun Number.toSpeedUnit(): Double {
    return msToKmh(this)
}

fun msToKmh(num: Number): Double {
    return num.toDouble() * 3.6
}