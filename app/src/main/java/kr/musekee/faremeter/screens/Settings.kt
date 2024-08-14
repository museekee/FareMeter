package kr.musekee.faremeter.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.preference.PreferenceManager
import kr.musekee.faremeter.R
import kr.musekee.faremeter.components.main.MainTitle
import kr.musekee.faremeter.components.main.SettingCategory
import kr.musekee.faremeter.components.main.SettingDropDown
import kr.musekee.faremeter.datas.transportations
import kr.musekee.faremeter.libs.SetPortrait

@Composable
fun Settings() {
    SetPortrait(LocalContext.current)
    val pref = PreferenceManager.getDefaultSharedPreferences(LocalContext.current)
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        MainTitle(
            title = stringResource(id = R.string.Settings)
        )
        SettingCategory(
            icon = R.drawable.ic_money,
            color = Color(0xFFFFE500),
            label = stringResource(R.string.Money)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 25.dp, top = 5.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            transportations.map {
                SettingDropDown(
                    icon = it.icon,
                    color = it.color,
                    name = stringResource(it.label),
                    values = it.calcTypes,
                    defaultValue = pref.getString("pref_${it.id}_calcType", it.calcTypes[0]) ?: it.calcTypes[0]) { newValue ->
                    Log.d("Settings", newValue)
                }
            }
        }
    }
}