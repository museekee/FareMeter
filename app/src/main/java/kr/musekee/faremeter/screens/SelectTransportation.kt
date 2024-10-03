package kr.musekee.faremeter.screens

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.musekee.faremeter.activities.TaxiActivity
import kr.musekee.faremeter.components.main.MainButton
import kr.musekee.faremeter.datas.taxi
import kr.musekee.faremeter.libs.DatabaseHelper
import kr.musekee.faremeter.libs.DrivingData
import kr.musekee.faremeter.libs.DrivingDataDao
import kr.musekee.faremeter.libs.RecordDao
import kr.musekee.faremeter.libs.RecordData
import kr.musekee.faremeter.libs.SetPortrait

@Composable
fun SelectTransportation() {
    val context = LocalContext.current
    SetPortrait(context)

    val recordDao = RecordDao(DatabaseHelper(LocalContext.current))
    val drivingDataDao = DrivingDataDao(DatabaseHelper(LocalContext.current))

    Column {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 50.sp,
            text = "교통수단 선택"
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            MainButton(
                label = stringResource(id = taxi.label),
                icon = taxi.icon,
                color = taxi.color,
                onClick = {
                    context.startActivity(Intent(context, TaxiActivity::class.java))
                }
            )
//            makeToast(LocalContext.current, "이거도ㅓㅣㅁ?")

            Button(
                onClick = {
                    recordDao.saveData(RecordData(
                        id = "TESTDATA",
                        fareCalcType = "경기도",
                        transportation = taxi.id,
                        endTime = 1726264574667,
                        fare = 195020,
                        distance = 233670.910437927
                    ))
                    val dts: List<Pair<Double, Long>> = listOf(
                        Pair(10.0, 1),
                        Pair(15.0, 2),
                        Pair(13.0, 3),
                        Pair(8.0, 5030),
                        Pair(0.0, 10000),
                        Pair(9.0, 11262),
                        Pair(79.0, 41600),
                        Pair(20.0, 42000),
                    )
                    dts.map {
                        drivingDataDao.addData(DrivingData(
                            id = "TESTDATA",
                            latitude = 37.5,
                            longitude = 130.5,
                            speed = it.first,
                            time = it.second
                        ))
                    }
                },
                enabled = false
            ) {
                Text(text = "비밀 버튼")
            }
        }
    }
}