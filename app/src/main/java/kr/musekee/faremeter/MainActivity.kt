package kr.musekee.faremeter

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.musekee.faremeter.components.taxi.TaxiButton
import kr.musekee.faremeter.components.taxi.TaxiButtonColor
import kr.musekee.faremeter.components.taxi.TaxiHorse
import kr.musekee.faremeter.ui.theme.FareMeterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FareMeterTheme {
                var speed by remember { mutableIntStateOf(0) }
                // A surface container using the 'background' color from the theme
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
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
                            label = "출발"
                        )
                        TaxiButton(
                            color = TaxiButtonColor.Red,
                            label = "도착"
                        )
                        TaxiButton(
                            color = TaxiButtonColor.Yellow,
                            label = "시외 할증"
                        )
                        TaxiButton(
                            color = TaxiButtonColor.Green,
                            label = "야간 할증"
                        )

                    }
                    Row(
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
                        LazyRow {
                            items(32) {
                                Button(onClick = { speed = it * 10 }) {
                                    Text((it * 10).toString())
                                }
                            }
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .background(Color(0xFF333333), RectangleShape),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Text(
                            fontWeight = FontWeight.Black,
                            fontSize = 17.sp,
                            color = Color.White,
                            text = "시외 할증 포함"
                        )
                        Text(
                            fontWeight = FontWeight.Black,
                            fontSize = 17.sp,
                            color = Color.White,
                            text = "주행 거리: 405.1km"
                        )
                        Text(
                            fontWeight = FontWeight.Black,
                            fontSize = 17.sp,
                            color = Color.White,
                            text = "야간 할증 포함"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FareMeterTheme {
        Greeting("Android")
    }
}