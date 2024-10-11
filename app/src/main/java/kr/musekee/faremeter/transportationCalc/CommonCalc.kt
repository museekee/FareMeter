package kr.musekee.faremeter.transportationCalc

import androidx.compose.runtime.MutableState
import kr.musekee.faremeter.transportationCalc.TaxiCalc.speed

interface CommonCalc {
    val fare: MutableState<Int>
    val counter: MutableState<Int>
    val distance: MutableState<Double> // m

    fun init(calcType: String) {
        fare.value = 0
        counter.value = 0
        distance.value = 0.0
    }

    fun increaseFare(curSpeed: Float) {
        speed.value = curSpeed
    }
}