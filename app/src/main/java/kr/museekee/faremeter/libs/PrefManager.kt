package kr.museekee.faremeter.libs

import android.content.Context
import androidx.preference.PreferenceManager
import kr.museekee.faremeter.datas.unknownTransportation

class PrefManager(private val context: Context) {
    var transportation: String
        set(id) {
            val pref = PreferenceManager.getDefaultSharedPreferences(context)
            val edit = pref.edit()
            edit.putString("pref_transportation", id)
            edit.apply()
        }
        get() {
            val pref = PreferenceManager.getDefaultSharedPreferences(context)
            return pref.getString("pref_transportation", unknownTransportation.id) ?: unknownTransportation.id
        }

    fun setCalcType(id: String, calcType: String) {
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        val edit = pref.edit()
        edit.putString("pref_${id}_calcType", calcType)
        edit.apply()
    }

    fun getCalcType(id: String): String {
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        return pref.getString("pref_${id}_calcType", "") ?: ""
    }

    var speedUnit: String
        set(unit) {
            val pref = PreferenceManager.getDefaultSharedPreferences(context)
            val edit = pref.edit()
            edit.putString("pref_speedUnit", unit)
            edit.apply()
        }
        get() {
            val pref = PreferenceManager.getDefaultSharedPreferences(context)
            return pref.getString("pref_speedUnit", "km/h") ?: "km/h"
        }
}