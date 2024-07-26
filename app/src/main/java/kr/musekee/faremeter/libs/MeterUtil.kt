package kr.musekee.faremeter.libs

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.preference.PreferenceManager
import kr.musekee.faremeter.datas.KTXData
import kr.musekee.faremeter.datas.TaxiData
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object MeterUtil {
    private lateinit var pref: SharedPreferences
    private var lastUpdateTime = 0L

    private var transportation = "taxi"
    private var minFare = 0
    private var minDistance = 0
    private var runFare = 0.0
    private var runDistance = 0
    private var timeFare = 0
    private var timeTime = 0
    private var intercityRate = 0.0
    private var nightRate: List<Double> = listOf()
    private var nightTime: List<List<Int>> = listOf()
    private var morningFare = 0
    private var sectionFare: List<Int> = listOf()
    private var sectionDistance: List<Int> = listOf()

    val fare: MutableState<Int> = mutableIntStateOf(0)

    val counter: MutableState<Int> = mutableIntStateOf(0)
    val distance: MutableState<Double> = mutableDoubleStateOf(0.0) // m
    val speed: MutableState<Float> = mutableFloatStateOf(0.0f) // m/s

    var isNight = false
    var isIntercity = false

    fun init(context: Context) {
        pref = PreferenceManager.getDefaultSharedPreferences(context)
        val locationPrefs = pref.getString("pref_location", null)
        val transportationPrefs = pref.getString("pref_transportation", "taxi")

        val standardTransportation = when (transportationPrefs) {
            "taxi" -> TaxiData.getDataByName(locationPrefs)
            "KTX" -> KTXData.getDataByName(locationPrefs)
            else -> TaxiData.getDataByName(locationPrefs)
        }
        transportation = transportationPrefs ?: "taxi"
        minFare = standardTransportation.minFare
        minDistance = standardTransportation.minDistance
        runFare = standardTransportation.runFare
        runDistance = standardTransportation.runDistance
        timeFare = standardTransportation.timeFare
        timeTime = standardTransportation.timeTime
        intercityRate = standardTransportation.intercityRate
        nightRate = standardTransportation.nightRate
        nightTime = standardTransportation.nightTime
        morningFare = standardTransportation.morningFare
        sectionFare = standardTransportation.sectionFare
        sectionDistance = standardTransportation.sectionDistance

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

    fun increaseFare(curSpeed: Float) {
        val curTime = System.currentTimeMillis()

        if (lastUpdateTime == 0L)
            lastUpdateTime = curTime

        val deltaTime = (curTime - lastUpdateTime).toInt() / 1000f
        lastUpdateTime = curTime

        speed.value = curSpeed

        val curDistance = speed.value * deltaTime
        distance.value += curDistance

        counter.value -= if (speed.value <= 4.2 && transportation == "taxi") { // 약 15km/h보다 느릴 때 (시간 요금), 택시일 때
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