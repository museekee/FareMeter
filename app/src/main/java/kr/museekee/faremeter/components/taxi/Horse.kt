package kr.museekee.faremeter.components.taxi

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.delay
import kr.museekee.faremeter.R
import kotlin.math.roundToLong

@Composable
fun TaxiHorse(speed: Double) { // km/h
    var imgIdx by remember { mutableIntStateOf(1) }
    val imgs = listOf(
        R.drawable.taxi_horse0,
        R.drawable.taxi_horse1,
        R.drawable.taxi_horse2,
        R.drawable.taxi_horse3,
        R.drawable.taxi_horse4,
        R.drawable.taxi_horse3,
        R.drawable.taxi_horse2,
        R.drawable.taxi_horse1,
    )
    LaunchedEffect(speed) {
        while (true) {
            // 120: 25 / 70: 50 / 30: 100 / 20 : 150 / 10이하: 200
            if (speed == 0.0) break
            val delayTime =
                if (speed < 30) 250 - (5 * speed)
                else if (speed < 70) 137.5 - (1.25 * speed)
                else if (speed < 120) 85 - (0.5 * speed)
                else if (speed < 305) 35 - (0.0833 * speed)
                else 5.0
            delay(delayTime.roundToLong())
            imgIdx = (imgIdx + 1) % imgs.size
        }
    }
    
    Image(
        painter = painterResource(id = imgs[imgIdx]), 
        contentDescription = "말"
    )
}