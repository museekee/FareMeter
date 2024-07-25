package kr.musekee.faremeter.libs

object MeterUtil {
    var totalSpeed = 0

    fun addSpeed(speed: Int) {
        totalSpeed += speed
    }
}