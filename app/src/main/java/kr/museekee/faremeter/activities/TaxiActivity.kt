package kr.museekee.faremeter.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.museekee.faremeter.R
import kr.museekee.faremeter.activities.ui.theme.FareMeterTheme
import kr.museekee.faremeter.components.KeepScreenOn
import kr.museekee.faremeter.components.RecordDialog
import kr.museekee.faremeter.components.taxi.TaxiButton
import kr.museekee.faremeter.components.taxi.TaxiButtonColor
import kr.museekee.faremeter.components.taxi.TaxiHorse
import kr.museekee.faremeter.datas.taxi
import kr.museekee.faremeter.libs.DatabaseHelper
import kr.museekee.faremeter.libs.GPSStatus
import kr.museekee.faremeter.libs.LocationService
import kr.museekee.faremeter.libs.MeterUtil
import kr.museekee.faremeter.libs.PrefManager
import kr.museekee.faremeter.libs.RecordDao
import kr.museekee.faremeter.libs.SetLandscape
import kr.museekee.faremeter.libs.toSpeedUnit
import kr.museekee.faremeter.libs.wonFormat
import kr.museekee.faremeter.transportationCalc.FareType
import kr.museekee.faremeter.transportationCalc.TaxiCalc
import kr.museekee.faremeter.ui.theme.lineSeedKr
import kotlin.math.round

class TaxiActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SetLandscape(this)
        val prefManager = PrefManager(this)

        if (!LocationService.isRunning) // 잠깐 앱을 나갔다 들어 왔을 때 초기화 되는거 방지
            MeterUtil.init(
                transportation = taxi.id,
                calcType = prefManager.getCalcType(taxi.id)
            )

        setContent {
            FareMeterTheme {
                KeepScreenOn()

                var useIntercity by remember { mutableStateOf(false) }
                var useNight by remember { mutableStateOf(false) }
                var dialogEnabled by remember { mutableStateOf(false) }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row( // TOP
                        modifier = Modifier
                            .size(720.dp, 70.dp)
                            .paint(
                                painterResource(id = R.drawable.taxi_top),
                                contentScale = ContentScale.FillBounds
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(
                            space = 15.dp,
                            alignment = Alignment.CenterHorizontally
                        )
                    ) {
                        TaxiButton(
                            color = TaxiButtonColor.Blue,
                            label = "출발",
                            enabled = !MeterUtil.isDriving.value,
                            pressed = MeterUtil.isDriving.value,
                            onClick = {
                                startService(Intent(this@TaxiActivity, LocationService::class.java))
                            }
                        )
                        TaxiButton(
                            color = TaxiButtonColor.Red,
                            label = "도착",
                            enabled = MeterUtil.isDriving.value,
                            pressed = !MeterUtil.isDriving.value,
                            onClick = {
                                stopService(Intent(this@TaxiActivity, LocationService::class.java))
                                dialogEnabled = true
                            }
                        )
                        TaxiButton(
                            color = TaxiButtonColor.Yellow,
                            label = "시외 할증",
                            pressed = useIntercity,
                            onClick = {
                                useIntercity = !useIntercity
                                TaxiCalc.toggleIntercity(useIntercity)
                            }
                        )
                        TaxiButton(
                            color = TaxiButtonColor.Green,
                            label = "야간 할증",
                            pressed = useNight,
                            onClick = {
                                useNight = !useNight
                                TaxiCalc.toggleNight(useNight)
                            }
                        )

                    }
                    Row( // Middle
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(horizontal = 70.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .size(210.dp, 190.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(
                                    space = 15.dp,
                                    alignment = Alignment.End
                                )
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .size(40.dp),
                                    imageVector = when (TaxiCalc.fareType.value) {
                                        FareType.TIME -> ImageVector.vectorResource(id = R.drawable.ic_timer)
                                        FareType.DISTANCE -> ImageVector.vectorResource(id = R.drawable.ic_speed)
                                    },
                                    contentDescription = when (TaxiCalc.fareType.value) {
                                        FareType.TIME -> "동시 병산제"
                                        FareType.DISTANCE -> "거리 비례제"
                                    },
                                    tint = Color.White
                                )
                                // GPS는 나도 잘 모르겠음
                                Icon(
                                    modifier = Modifier
                                        .size(40.dp),
                                    imageVector = when (MeterUtil.gpsStatus.value) {
                                        GPSStatus.STABLE -> ImageVector.vectorResource(id = R.drawable.ic_satellite)
                                        GPSStatus.UNSTABLE -> ImageVector.vectorResource(id = R.drawable.ic_satellite_no)
                                    },
                                    contentDescription = when (MeterUtil.gpsStatus.value) {
                                        GPSStatus.STABLE -> "GPS 연결됨"
                                        GPSStatus.UNSTABLE -> "GPS 불안정"
                                    },
                                    tint = Color.White
                                )
                            }
                            TaxiHorse(
                                speed = TaxiCalc.speed.value.toSpeedUnit("km/h")
                            )
                        }
                        Spacer(Modifier.weight(1f))
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                            ) {
                                Text(
                                    fontSize = 35.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF0055FF),
                                    text = "${TaxiCalc.counter.value}m"
                                )
                                LinearProgressIndicator(
                                    modifier = Modifier
                                        .background(
                                            color = Color(0xFF333333),
                                            shape = RoundedCornerShape(100)
                                        )
                                        .height(15.dp)
                                        .clip(RoundedCornerShape(100)),
                                    progress = if (TaxiCalc._counter.value == 0) 0f else 1f - (TaxiCalc.counter.value.toFloat() / TaxiCalc._counter.value.toFloat()),
                                    trackColor = Color(0xFF333333),
                                    color = Color(0xFF0055FF)
                                )
                            }
                            Text(
                                fontSize = 80.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                text = "${TaxiCalc.fare.value.wonFormat()}원"
                            )
                            Text(
                                fontSize = 45.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF27E43A),
                                text = "${(TaxiCalc.speed.value.toSpeedUnit(prefManager.speedUnit) * 10f).toInt() / 10f} ${prefManager.speedUnit}",
                                textAlign = TextAlign.Right
                            )
                        }
                    }
                    Row( // Bottom
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .background(Color(0xFF333333), RectangleShape),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Text(
                            modifier = Modifier
                                .alpha(if (useIntercity) 1f else 0f),
                            fontFamily = lineSeedKr,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.White,
                            text = "시외 할증 포함"
                        )
                        Text(
                            fontFamily = lineSeedKr,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.White,
                            text = "주행 거리: ${round(TaxiCalc.distance.value / 100) / 10}km"
                        )
                        Text(
                            modifier = Modifier
                                .alpha(if (useNight) 1f else 0f),
                            fontFamily = lineSeedKr,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.White,
                            text = "야간 할증 포함"
                        )
                    }
                }
                if (dialogEnabled) {
                    val recordDao = RecordDao(DatabaseHelper(LocalContext.current))
                    val dialogData = recordDao
                        .getAllData()
                        .orderBy("END_TIME", "DESC")
                        .limit(1)
                        .execute()[0]
                    RecordDialog(
                        id = dialogData.id,
                        onDismiss = { dialogEnabled = false },
                        onDeleteButtonClick = {
                            recordDao.deleteData(dialogData.id)
                        },
                        onCloseBtnClick = {}
                    )
                }
            }
        }
    }
}