package kr.musekee.faremeter.activities

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.musekee.faremeter.R
import kr.musekee.faremeter.activities.ui.theme.FareMeterTheme
import kr.musekee.faremeter.components.taxi.TaxiButton
import kr.musekee.faremeter.components.taxi.TaxiButtonColor
import kr.musekee.faremeter.components.taxi.TaxiHorse
import kr.musekee.faremeter.libs.LocationService
import kr.musekee.faremeter.libs.MeterUtil
import kr.musekee.faremeter.libs.SetLandscape
import kr.musekee.faremeter.transportationCalc.TaxiCalc

class TaxiActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SetLandscape(this)
        setContent {
            FareMeterTheme {
                var useIntercity by remember { mutableStateOf(false) }
                var useNight by remember { mutableStateOf(false) }
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
                            onClick = {
                                startService(Intent(this@TaxiActivity, LocationService::class.java))
                                MeterUtil.init(this@TaxiActivity)
                            }
                        )
                        TaxiButton(
                            color = TaxiButtonColor.Red,
                            label = "도착",
                            onClick = {
                                stopService(Intent(this@TaxiActivity, LocationService::class.java))
                            }
                        )
                        TaxiButton(
                            color = TaxiButtonColor.Yellow,
                            label = "시외 할증",
                            onClick = {
                                useIntercity = !useIntercity
                                TaxiCalc.toggleIntercity(useIntercity)
                            }
                        )
                        TaxiButton(
                            color = TaxiButtonColor.Green,
                            label = "야간 할증",
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
                        TaxiHorse(
                            speed = TaxiCalc.speed.value.toDouble()
                        )
                        Spacer(Modifier.weight(1f))
                        Column {
                            Text(
                                fontSize = 35.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF0055FF),
                                text = "${TaxiCalc.counter.value}m"
                            )
                            Text(
                                fontSize = 80.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                text = "${TaxiCalc.fare.value}원"
                            )
                            Text(
                                fontSize = 45.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF27E43A),
                                text = "${((TaxiCalc.speed.value * 3.6f) * 10f).toInt() / 10f} km/h",
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
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp,
                            color = Color.White,
                            text = "시외 할증 포함"
                        )
                        Text(
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp,
                            color = Color.White,
                            text = "주행 거리: ${(TaxiCalc.distance.value / 100).toInt() / 10}km"
                        )
                        Text(
                            modifier = Modifier
                                .alpha(if (useNight) 1f else 0f),
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp,
                            color = Color.White,
                            text = "야간 할증 포함"
                        )
                    }
                }
            }
        }
    }
}