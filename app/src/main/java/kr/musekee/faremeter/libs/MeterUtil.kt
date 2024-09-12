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
    private var lastUpdateTime: Long? = null
    private var lastPosition = Pair(0.0, 0.0)

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

    // (이전 시간, 현재 시간), (이전 위도, 현재 위도), (이전 경도, 현재 경도)
    fun checkNoGps(latitude: Double, longitude: Double): Triple<Pair<Long, Long>, Pair<Double, Double>, Pair<Double, Double>>? {
        val curTime = System.currentTimeMillis()
        if (lastUpdateTime == null)
            return null
        if (curTime - lastUpdateTime!! < 5000) // 새로고침 하는데 5초를 넘으면 GPS가 중간에 끊긴거라고 봄.
            return null
        val result = Triple(Pair(lastUpdateTime!!, curTime), Pair(lastPosition.first, latitude), Pair(lastPosition.second, longitude))
        lastUpdateTime = curTime
        lastPosition = Pair(latitude, longitude)
        return result
    }
}