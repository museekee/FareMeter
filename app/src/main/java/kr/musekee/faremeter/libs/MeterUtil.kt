package kr.musekee.faremeter.libs

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import kr.musekee.faremeter.datas.getTransportationById
import kr.musekee.faremeter.datas.unknownTransportation
import kr.musekee.faremeter.transportationCalc.CommonCalc
import kr.musekee.faremeter.transportationCalc.TaxiCalc

enum class GPSStatus {
    STABLE,
    UNSTABLE
}

object MeterUtil {
    private var transportation = unknownTransportation.id

    private lateinit var calc: CommonCalc
    private val taxiCalc = TaxiCalc

    var fare: MutableState<Int> = mutableIntStateOf(0)
    var distance: MutableState<Double> = mutableDoubleStateOf(0.0) // m
    var speed: MutableState<Float> = mutableFloatStateOf(0.0f)
    var gpsStatus: MutableState<GPSStatus> = mutableStateOf(GPSStatus.UNSTABLE)
    var isDriving: MutableState<Boolean> = mutableStateOf(false)

    fun init(transportation: String, calcType: String) {
        this@MeterUtil.transportation = transportation

        taxiCalc.init(calcType)
        calc = getTransportationById(transportation).calc
        calc.init(calcType)
        gpsStatus.value = GPSStatus.UNSTABLE
        resetValues()
    }

    fun resetValues() {
        gpsStatus.value = GPSStatus.UNSTABLE
        fare.value = 0
        distance.value = 0.0
        speed.value = 0.0f
        taxiCalc.resetValues()
    }

    fun increaseFare(curSpeed: Float) {
        calc.increaseFare(curSpeed)
        gpsStatus.value = GPSStatus.STABLE
        fare.value = calc.fare.value
        distance.value = calc.distance.value
        speed.value = curSpeed
    }
}