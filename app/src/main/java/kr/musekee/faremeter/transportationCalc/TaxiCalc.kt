package kr.musekee.faremeter.transportationCalc

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.preference.PreferenceManager
import kr.musekee.faremeter.datas.TaxiData
import kr.musekee.faremeter.datas.TaxiTransportation
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


object TaxiCalc {
    private var lastUpdateTime = 0L
    private lateinit var pref: SharedPreferences

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

    val counter: MutableState<Int> = mutableIntStateOf(0)
    val distance: MutableState<Double> = mutableDoubleStateOf(0.0) // m
    val speed: MutableState<Float> = mutableFloatStateOf(0.0f) // m/s

    private var isNight = false
    private var isIntercity = false

    private lateinit var transportationData: TaxiTransportation

    fun init(context: Context) {
        pref = PreferenceManager.getDefaultSharedPreferences(context)
        val locationPrefs = pref.getString("pref_location", null)
        transportationData = TaxiData.getDataByName(locationPrefs)

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
        counter.value = minDistance
        distance.value = 0.0
        speed.value = 0.0f

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
        distance.value += curDistance

        counter.value -= if (speed.value <= 4.2) { // 약 15km/h보다 느릴 때 (시간 요금), 택시일 때
            (runDistance / timeTime * deltaTime).toInt()
        } else {
            curDistance.toInt()
        }

        if (counter.value <= 0) {
            val nowHour = SimpleDateFormat("HH", Locale.getDefault()).format(Calendar.getInstance().time).toInt()
            val nightIndex = nightTime.indexOfFirst { it.any { inner -> inner == nowHour } }
            val nightFare = if (isNight) nightRate[nightIndex] * runFare else 0.0
            val intercityFare = if (isIntercity) intercityRate * runFare else 0.0
            fare.value += (runFare + nightFare + intercityFare).toInt()
            counter.value = runDistance
        }

        speed.value = curSpeed

        return Triple(fare, distance, speed)
    }

    fun toggleNight(enabled: Boolean) {
        isNight = enabled
        val nowHour = SimpleDateFormat("HH", Locale.getDefault()).format(Calendar.getInstance().time).toInt()
        val nightIndex = nightTime.indexOfFirst { it.any { inner -> inner == nowHour } }
        val nightFare = if (nightIndex == -1) 0 else (nightRate[nightIndex] * minFare.toDouble()).toInt()
        if (enabled)
            fare.value += nightFare
        else
            fare.value -= nightFare
    }

    fun toggleIntercity(enabled: Boolean) {
        isIntercity = enabled
        val intercityFare = (intercityRate * minFare.toDouble()).toInt()
        if (enabled)
            fare.value += intercityFare
        else
            fare.value -= intercityFare
    }
}