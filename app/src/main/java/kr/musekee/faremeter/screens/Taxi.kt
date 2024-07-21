package kr.musekee.faremeter.screens

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.musekee.faremeter.R
import kr.musekee.faremeter.components.taxi.TaxiButton
import kr.musekee.faremeter.components.taxi.TaxiButtonColor
import kr.musekee.faremeter.components.taxi.TaxiHorse
import kr.musekee.faremeter.libs.SetLandscape

@Composable
fun Taxi() {
    SetLandscape(LocalContext.current)
    var speed by remember { mutableIntStateOf(0) }
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
                onClick = {}
            )
            TaxiButton(
                color = TaxiButtonColor.Red,
                label = "도착",
                onClick = {}
            )
            TaxiButton(
                color = TaxiButtonColor.Yellow,
                label = "시외 할증",
                onClick = {
                    useIntercity = !useIntercity
                }
            )
            TaxiButton(
                color = TaxiButtonColor.Green,
                label = "야간 할증",
                onClick = {
                    useNight = !useNight
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
                speed = speed.toDouble()
            )
            Spacer(Modifier.weight(1f))
            Column() {
                Text(
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF0055FF),
                    text = "1600m"
                )
                Text(
                    fontSize = 80.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    text = "4800원"
                )
                Text(
                    fontSize = 45.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF27E43A),
                    text = "0.0 km/h",
                    textAlign = TextAlign.Right
                )
            }
//                        LazyRow {
//                            items(32) {
//                                Button(onClick = { speed = it * 10 }) {
//                                    Text((it * 10).toString())
//                                }
//                            }
//                        }
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
                text = "주행 거리: 405.1km"
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