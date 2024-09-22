package kr.musekee.faremeter.transportationCalc

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import kr.musekee.faremeter.datas.TaxiData
import kr.musekee.faremeter.datas.TaxiTransportation
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.abs

enum class FareType {
    TIME,
    DISTANCE
}

object TaxiCalc {
    private var lastUpdateTime = 0L

    private var minFare = 0
    private var minDistance = 0
    private var runFare = 0.0
    private var runDistance = 0
    private var timeFare = 0
    private var timeTime = 0
    private var intercityRate = 0.0
    private var nightRate: List<Double> = listOf()
    private var nightTime: List<List<Int>> = listOf()

    val fare: MutableState<Int> = mutableIntStateOf(0)
    val nightFare: MutableState<Int> = mutableIntStateOf(0)
    val intercityFare: MutableState<Int> = mutableIntStateOf(0)

    val counter: MutableState<Int> = mutableIntStateOf(0)
    val distance: MutableState<Double> = mutableDoubleStateOf(0.0) // m
    val speed: MutableState<Float> = mutableFloatStateOf(0.0f) // m/s
    val _counter: MutableState<Int> = mutableIntStateOf(0) // counter 최대값
    val fareType: MutableState<FareType> = mutableStateOf(FareType.TIME)

    private var isNight = false
    private var isIntercity = false

    private lateinit var transportationData: TaxiTransportation

    fun init(calcType: String) {
        transportationData = TaxiData.getData(calcType)

        transportationData.let {
            minFare = it.minFare
            minDistance = it.minDistance
            runFare = it.runFare
            runDistance = it.runDistance
            timeFare = it.timeFare
            timeTime = it.timeTime
            intercityRate = it.intercityRate
            nightRate = it.nightRate
            nightTime = it.nightTime
        }

        resetValues()
    }

    fun resetValues() {
        fare.value = minFare
        nightFare.value = 0
        intercityFare.value = 0
        counter.value = minDistance
        _counter.value = counter.value
        distance.value = 0.0
        speed.value = 0.0f
        lastUpdateTime = 0L

        isNight = false
        isIntercity = false
    }

    fun increaseFare(curSpeed: Float): Triple<MutableState<Int>, MutableState<Double>, MutableState<Float>> {
        val curTime = System.currentTimeMillis()

        if (lastUpdateTime == 0L)
            lastUpdateTime = curTime

        val deltaTime = (curTime - lastUpdateTime).toInt() / 1000f
        lastUpdateTime = curTime

        speed.value = curSpeed

        val curDistance = speed.value * deltaTime
        distance.value += curDistance // m

        counter.value -= if (speed.value <= 4.2) { // 약 15km/h보다 느릴 때 (시간 요금 + 거리 요금), 택시일 때
            fareType.value = FareType.TIME
            (runDistance / timeTime * deltaTime).toInt() + curDistance.toInt() // 시간 요금 + 거리 요금
        } else {
            fareType.value = FareType.DISTANCE
            curDistance.toInt() // 거리 요금
        }

        if (counter.value <= 0) {
            val nowHour = SimpleDateFormat("HH", Locale.getDefault()).format(Calendar.getInstance().time).toInt()
            val nightIndex = nightTime.indexOfFirst { it.any { inner -> inner == nowHour } }
            val nightAdditionalFare = if (isNight && nightIndex != -1) nightRate[nightIndex] * runFare else 0.0
            val intercityAdditionalFare = if (isIntercity) intercityRate * runFare else 0.0
            nightFare.value += nightAdditionalFare.toInt()
            intercityFare.value += intercityAdditionalFare.toInt()
            fare.value += (runFare + nightAdditionalFare + intercityAdditionalFare).toInt()
            counter.value += (abs(counter.value % runDistance) + 1) * runDistance
            _counter.value = runDistance
        }

        speed.value = curSpeed

        return Triple(fare, distance, speed)
    }

    fun toggleNight(enabled: Boolean) {
        isNight = enabled
        val nowHour = SimpleDateFormat("HH", Locale.getDefault()).format(Calendar.getInstance().time).toInt()
        val nightIndex = nightTime.indexOfFirst { it.any { inner -> inner == nowHour } }
        val nightMinFare = if (nightIndex == -1) 0 else (nightRate[nightIndex] * minFare.toDouble()).toInt()
        if (enabled)
            fare.value += nightMinFare + nightFare.value
        else
            fare.value -= nightMinFare + nightFare.value
    }

    fun toggleIntercity(enabled: Boolean) {
        isIntercity = enabled
        val intercityMinFare = (intercityRate * minFare.toDouble()).toInt()
        if (enabled)
            fare.value += intercityMinFare + intercityFare.value
        else
            fare.value -= intercityMinFare + intercityFare.value
    }
}