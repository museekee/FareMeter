package kr.museekee.faremeter.components.taxi

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
import kr.museekee.faremeter.R
import kr.museekee.faremeter.ui.theme.lineSeedKr

@Composable
fun TaxiButton(
    color: TaxiButtonColor,
    label: String,
    enabled: Boolean = true,
    pressed: Boolean = false,
    onClick: () -> Unit
) {
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
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent
        ),
        enabled = enabled,
        onClick = onClick
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .clip(RectangleShape)
        ) {
            val width = maxWidth - 4.dp
            val height = maxHeight - 4.dp
            Image(
                painter = painterResource(
                    id = if (!pressed) R.drawable.taxi_button else R.drawable.taxi_button_pressed
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
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                text = label,
                fontFamily = lineSeedKr,
                color = Color(0xFF444444)
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