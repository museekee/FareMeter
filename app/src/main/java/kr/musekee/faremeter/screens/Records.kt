package kr.musekee.faremeter.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import kr.musekee.faremeter.R
import kr.musekee.faremeter.components.main.MainTitle
import kr.musekee.faremeter.components.main.ResultItem
import kr.musekee.faremeter.libs.DatabaseHelper
import kr.musekee.faremeter.libs.RecordDao

@Composable
fun Results() {
    val scrollState = rememberScrollState()

    Column {
        MainTitle(
            title = stringResource(id = R.string.Results)
        )
        Row {

        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(scrollState)
        ) {
            RecordDao(DatabaseHelper(LocalContext.current)).getAllData(
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