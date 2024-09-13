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
import kr.musekee.faremeter.libs.TimePosition

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
                    recordDao.saveData(RecordData(
                        _id = 0,
                        fareCalcType = "경기도",
                        transportation = taxi.id,
                        fare = 4800,
                        averageSpeed = 15.5f,
                        topSpeed = 46.1f,
                        distance = 436.0,
                        timePos = listOf(
                            TimePosition(36.990497, 126.913105, 1726242392281),
                            TimePosition(36.990087, 126.914300, 1726242392281),
                            TimePosition(36.989777, 126.915167, 1726242392281),
                            TimePosition(36.989487, 126.916010, 1726242392281),
                            TimePosition(36.988596, 126.916103, 1726242392281),
                            TimePosition(36.988035, 126.916127, 0),
                            TimePosition(36.986425, 126.917321, 1726242392281),
                            TimePosition(36.986432, 126.917930, 1726242392281),
                            TimePosition(36.986457, 126.918782, 0),
                            TimePosition(36.986001, 126.919281, 1726242392281),
                            TimePosition(36.985664, 126.919281, 1726242392281),
                            TimePosition(36.984542, 126.919289, 1726242392281),
                            TimePosition(36.984548, 126.919961, 1726242392281),
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