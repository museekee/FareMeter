package kr.musekee.faremeter.libs

fun Number.toSpeedUnit(speedUnit: String): Double {
    return when (speedUnit) {
        "km/h" -> msToKmh(this)
        "mph" -> msToMph(this)
        "m/s" -> msToms(this)
        "Knot" -> msToKnot(this)
        else -> msToKmh(this)
    }
}

fun msToKmh(num: Number): Double {
    return num.toDouble() * 3.6
}
fun msToMph(num: Number): Double {
    return num.toDouble() * 2.237
}
fun msToms(num: Number): Double {
    return num.toDouble()
}
fun msToKnot(num: Number): Double {
    return num.toDouble() * 1.944
}