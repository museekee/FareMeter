package kr.musekee.faremeter.libs

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.preference.PreferenceManager
import kr.musekee.faremeter.transportationCalc.TaxiCalc

object MeterUtil {
    private lateinit var pref: SharedPreferences

    private var transportation = "taxi"

    private val taxiCalc = TaxiCalc

    var fare = mutableIntStateOf(0)
    var distance = mutableDoubleStateOf(0.0)
    var speed = mutableFloatStateOf(0.0f)

    fun init(context: Context) {
        pref = PreferenceManager.getDefaultSharedPreferences(context)

        transportation = pref.getString("pref_transportation", "taxi") ?: "taxi"

        taxiCalc.init(context)
        resetValues()
    }

    fun resetValues() {
        taxiCalc.resetValues()
    }

    fun increaseFare(curSpeed: Float) {
        val (mfare, mdistance, mspeed) = when (transportation) {
            "taxi" -> taxiCalc.increaseFare(curSpeed)
            else -> Triple(mutableIntStateOf(0), mutableDoubleStateOf(0.0), mutableFloatStateOf(0.0f))
        }
        fare.intValue = mfare.value
        distance.doubleValue = mdistance.value
        speed.floatValue = mspeed.value
    }
}