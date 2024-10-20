package kr.museekee.faremeter.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import kr.museekee.faremeter.R
import kr.museekee.faremeter.components.main.MainTitle
import kr.museekee.faremeter.components.main.SettingDropDown
import kr.museekee.faremeter.components.main.SettingRow
import kr.museekee.faremeter.datas.transportations
import kr.museekee.faremeter.libs.PrefManager
import kr.museekee.faremeter.libs.SetPortrait

@Composable
fun Settings() {
    SetPortrait(LocalContext.current)
    val prefManager = PrefManager(LocalContext.current)
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        MainTitle(
            title = stringResource(id = R.string.Settings)
        )
        SettingRow(
            icon = R.drawable.ic_money,
            color = Color(0xFFFFE500),
            title = stringResource(R.string.Money)
        ) {
            transportations.map {
                SettingDropDown(
                    icon = it.icon,
                    color = it.color,
                    name = stringResource(it.label),
                    values = it.calcTypes,
                    dropDownTitle = "요금 체계 선택",
                    defaultValue = prefManager.getCalcType(it.id)) { newValue ->
                    prefManager.setCalcType(it.id, newValue)
                }
            }
        }
        SettingRow(
            icon = R.drawable.ic_speed,
            color = Color(0xFFEB5656),
            title = stringResource(R.string.MeterSetting)
        ) {
            SettingDropDown(
                icon = R.drawable.ic_calculator,
                color = Color(0xFF9500FF),
                name = stringResource(R.string.Unit),
                values = listOf("km/h", "mph", "m/s", "Knot"),
                dropDownTitle = "속도 단위 선택",
                defaultValue = prefManager.speedUnit) { newValue ->
                prefManager.speedUnit = newValue
            }
        }
    }
}