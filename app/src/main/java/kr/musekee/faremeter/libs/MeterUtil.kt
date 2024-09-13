package kr.musekee.faremeter.libs

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import kr.musekee.faremeter.datas.taxi
import kr.musekee.faremeter.datas.unknownTransportation
import kr.musekee.faremeter.transportationCalc.TaxiCalc

enum class GPSStatus {
    STABLE,
    UNSTABLE
}

object MeterUtil {
    private var transportation = unknownTransportation.id

    private val taxiCalc = TaxiCalc

    var fare: MutableState<Int> = mutableIntStateOf(0)
    var distance: MutableState<Double> = mutableDoubleStateOf(0.0) // m
    var speed: MutableState<Float> = mutableFloatStateOf(0.0f)
    var gpsStatus: MutableState<GPSStatus> = mutableStateOf(GPSStatus.UNSTABLE)
    var isDriving: MutableState<Boolean> = mutableStateOf(false)

    fun init(transportation: String, calcType: String) {
        this@MeterUtil.transportation = transportation

        taxiCalc.init(calcType)
        gpsStatus.value = GPSStatus.UNSTABLE
        resetValues()
    }

    fun resetValues() {
        gpsStatus.value = GPSStatus.UNSTABLE
        taxiCalc.resetValues()
    }

    fun increaseFare(curSpeed: Float) {
        val (mFare, mDistance, mSpeed) = when (transportation) {
            taxi.id -> taxiCalc.increaseFare(curSpeed)
            else -> Triple(mutableIntStateOf(0), mutableDoubleStateOf(0.0), mutableFloatStateOf(0.0f))
        }
        gpsStatus.value = GPSStatus.STABLE
        fare.value = mFare.value
        distance.value = mDistance.value
        speed.value = mSpeed.value
    }
}