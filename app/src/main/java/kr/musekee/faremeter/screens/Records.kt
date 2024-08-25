package kr.musekee.faremeter.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import kr.musekee.faremeter.components.RecordDialog
import kr.musekee.faremeter.components.main.MainTitle
import kr.musekee.faremeter.components.main.RecordItem
import kr.musekee.faremeter.datas.taxi
import kr.musekee.faremeter.libs.DatabaseHelper
import kr.musekee.faremeter.libs.RecordDao
import kr.musekee.faremeter.libs.RecordData
import java.util.Calendar
import java.util.Date

@Composable
fun Results() {
    val scrollState = rememberScrollState()
    var dialogEnabled by remember { mutableStateOf(false) }
    var dialogData by remember { mutableStateOf(RecordData(1, "서울특별시", "TAXI", Date(), Date(), 10000, 30f, 100f, 50.51)) }
    val recordDao = RecordDao(DatabaseHelper(LocalContext.current))
    var limit by remember { mutableIntStateOf(50) }
    var transportation by remember { mutableStateOf(taxi.id) } // 전체 보기는 null
    var dateOrder by remember { mutableStateOf("DESC") }

    Column {
        MainTitle(
            title = stringResource(id = R.string.Results)
        )
        Row { // 여기서 정렬 방식 선택, #dp tdp fWeight ddp

        }
        Column( // lazycolumn으로 바꾸고
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(scrollState)
        ) {
            // #에서 limit 설정하기
            // 일단 운임은 orderby 빼자.
            recordDao.getAllData()
                .limit(limit)
                .transportation(transportation)
                .orderBy("END_TIME", dateOrder)
                .execute()
                .mapIndexed { index, it ->
                RecordItem(index, it.transportation, it.fare, it.distance, it.endTime) {
                    dialogData = it
                    dialogEnabled = true
                }
            }
        }
    }

    if (dialogEnabled)
        RecordDialog(
            data = dialogData,
            onDismiss = { dialogEnabled = false },
            onDeleteButtonClick = {
                recordDao.deleteData(dialogData._id)
            },
            onCloseBtnClick = {}
        )

}

fun convertToMinutes(date: Date): Int {
    val calendar = Calendar.getInstance()
    calendar.time = date

    val hour = calendar.get(Calendar.HOUR_OF_DAY) % 12
    val minute = calendar.get(Calendar.MINUTE)

    return hour * 60 + minute
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