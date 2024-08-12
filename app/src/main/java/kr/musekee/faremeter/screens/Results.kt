package kr.musekee.faremeter.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.musekee.faremeter.R
import kr.musekee.faremeter.components.main.ResultItem
import kr.musekee.faremeter.libs.RecordSql
import kr.musekee.faremeter.ui.theme.lineSeedKr

@Composable
fun Results() {
    val scrollState = rememberScrollState()

    Column {
        Text( // 제목
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .bottomBorder(1.dp, Color(0xFF777777))
                .wrapContentHeight(align = Alignment.CenterVertically),
            text = stringResource(id = R.string.Results),
            fontWeight = FontWeight.Bold,
            fontFamily = lineSeedKr,
            fontSize = 17.sp,
            textAlign = TextAlign.Center
        )
        Row {

        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(scrollState)
        ) {
            RecordSql(LocalContext.current).getAllData(
                type = null,
                limit = null
            ).mapIndexed { index, it ->
                ResultItem(index, it.transportation, it.fare, it.distance, it.endTime)
            }
        }
    }
}

fun Modifier.bottomBorder(strokeWidth: Dp, color: Color): Modifier = composed {
    val density = LocalDensity.current
    val strokeWidthPx = density.run { strokeWidth.toPx() }

    this then Modifier.drawBehind {
        val width = size.width
        val height = size.height - strokeWidthPx / 2

        drawLine(
            color = color,
            start = Offset(x = 0f, y = height),
            end = Offset(x = width , y = height),
            strokeWidth = strokeWidthPx
        )
    }
}