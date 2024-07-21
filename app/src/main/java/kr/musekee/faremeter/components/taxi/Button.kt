package kr.musekee.faremeter.components.taxi

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.musekee.faremeter.R

@Composable
fun TaxiButton(color: TaxiButtonColor, label: String) {
    Button(
        contentPadding = PaddingValues(),
        modifier = Modifier
            .width(120.dp)
            .height(60.dp)
            .defaultMinSize(
                minWidth = 1.dp,
                minHeight = 1.dp
            ),
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        onClick = { /*TODO*/ }
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .clip(RectangleShape)
        ) {
            val width = maxWidth - 4.dp
            val height = maxHeight - 4.dp
            Image(
                painter = painterResource(
                    id = R.drawable.taxi_button
                ), 
                contentDescription = null,
                colorFilter = ColorFilter.tint(
                    color = Color(color.color),
                    blendMode = BlendMode.Modulate
                )
            )
            Text(
                modifier = Modifier
                    .size(width, height)
                    .wrapContentHeight(align = Alignment.CenterVertically),
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,
                text = label
            )
        }
    }
}

enum class TaxiButtonColor(val color: Long) {
    Blue(0xFFB1C3EF),
    Red(0xFFFF5555),
    Yellow(0xFFF1F43C),
    Green(0xFF27E43A)
}