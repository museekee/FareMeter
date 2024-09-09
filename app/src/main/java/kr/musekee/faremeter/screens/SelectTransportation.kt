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
import kr.musekee.faremeter.libs.RecordDao
import kr.musekee.faremeter.libs.RecordData
import kr.musekee.faremeter.libs.SetPortrait
import java.util.Calendar

@Composable
fun SelectTransportation() {
    val context = LocalContext.current
    SetPortrait(context)

    val recordDao = RecordDao(DatabaseHelper(LocalContext.current))

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
            
            Button(
                onClick = {
                    val calendar = Calendar.getInstance()
                    calendar.set(2024, Calendar.SEPTEMBER, 9, 22, 15, 51)
                    val startTime = calendar.time
                    calendar.set(2024, Calendar.SEPTEMBER, 9, 22, 17, 36)
                    val endTime = calendar.time
                    recordDao.saveData(RecordData(
                        _id = 0,
                        fareCalcType = "경기도",
                        transportation = taxi.id,
                        startTime = startTime,
                        endTime = endTime,
                        fare = 4800,
                        averageSpeed = 15.5f,
                        topSpeed = 46.1f,
                        distance = 436.0,
                        latitudes = listOf(
                            36.978051,
                            36.978207,
                            36.978614,
                            36.978733,
                            36.979611,
                            36.980475,
                            36.980742
                        ),
                        longitudes = listOf(
                            126.925266,
                            126.925350,
                            126.925684,
                            126.925783,
                            126.926501,
                            126.927234,
                            126.926737
                        )
                    ))
                },
//                enabled = false
            ) {
                Text(text = "비밀 버튼")
            }
        }
    }
}