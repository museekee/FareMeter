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
                    calendar.set(2024, Calendar.SEPTEMBER, 9, 22, 25, 36)
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
                            36.990497,
                            36.990087,
                            36.989777,
                            36.989487,
                            36.988596,
                            36.988035,
                            36.986425,
                            36.986432,
                            36.986457,
                            36.986001,
                            36.985664,
                            36.984542,
                            36.984548
                        ),
                        longitudes = listOf(
                            126.913105,
                            126.914300,
                            126.915167,
                            126.916010,
                            126.916103,
                            126.916127,
                            126.917321,
                            126.917930,
                            126.918782,
                            126.919281,
                            126.919281,
                            126.919289,
                            126.919961
                        ),
                        noGPSLatitudes = listOf(
                            Pair(36.988035, 36.986425),
                            Pair(36.986457, 36.986001),
                        ),
                        noGPSLongitudes = listOf(
                            Pair(126.916127, 126.917321),
                            Pair(126.918782, 126.919281)
                        ),
                        noGPSTimes = listOf(
                            Pair(154641656, 458664856)
                        )
                    ))
                },
                enabled = false
            ) {
                Text(text = "비밀 버튼")
            }
        }
    }
}