package kr.musekee.faremeter.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kr.musekee.faremeter.R
import kr.musekee.faremeter.components.main.MainTitle
import kr.musekee.faremeter.components.main.RecordDialogContainer
import kr.musekee.faremeter.components.main.RecordItem
import kr.musekee.faremeter.components.main.RecordOtherInfo
import kr.musekee.faremeter.datas.getTransportationById
import kr.musekee.faremeter.libs.DatabaseHelper
import kr.musekee.faremeter.libs.PrefManager
import kr.musekee.faremeter.libs.RecordDao
import kr.musekee.faremeter.libs.RecordData
import kr.musekee.faremeter.libs.cutForSpeed
import kr.musekee.faremeter.libs.toSpeedUnit
import kr.musekee.faremeter.ui.theme.lineSeedKr
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

@Composable
fun Results() {
    val prefManager = PrefManager(LocalContext.current)
    val scrollState = rememberScrollState()
    var dialogEnabled by remember { mutableStateOf(false) }
    var dialogData by remember { mutableStateOf(RecordData(1, "서울특별시", "TAXI", Date(), Date(), 10000, 30f, 100f, 50.51)) }

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
                transportation = null,
                limit = null
            ).mapIndexed { index, it ->
                RecordItem(index, it.transportation, it.fare, it.distance, it.endTime) {
                    dialogData = it
                    dialogEnabled = true
                }
            }
        }
    }

    if (dialogEnabled)
        Dialog(onDismissRequest = {
            dialogEnabled = false
        }) {
            val transportation = getTransportationById(dialogData.transportation)
            val transportationColor = transportation.color
            val startTime = dialogData.startTime
            val endTime = dialogData.endTime
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .width(300.dp)
                    .height(500.dp)
                    .padding(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF202020)
                ),
                shape = RoundedCornerShape(25.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text(
                        text = "${dialogData.fare}원",
                        fontSize = 50.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFDE4D),
                        fontFamily = lineSeedKr,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .drawBehind {
                                drawLine(
                                    Color(0xFFFFDE4D),
                                    Offset(0f, 0f),
                                    Offset(75.dp.toPx(), 0f),
                                    8.dp.toPx()
                                )
                                drawLine(
                                    Color(0xFFFFFFFF),
                                    Offset(size.width - 50.dp.toPx(), size.height),
                                    Offset(size.width, size.height),
                                    4.dp.toPx()
                                )
                            }
                            .padding(vertical = 15.dp)
                            .fillMaxWidth()
                    )
                    //region 그래프들
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        RecordDialogContainer(
                            title = "시간"
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                            ) {
                                //region 원 그래프
                                Canvas(
                                    modifier = Modifier
                                        .size(100.dp)
                                ) {
                                    // 1분 당 0.5도
                                    val startAngle = (convertToMinutes(dialogData.startTime) / 2f) - 90 // 기본 0도는 3시 방향인 듯...
                                    val endAngle = (convertToMinutes(dialogData.endTime) / 2f) - 90
                                    val sweepAngle = if (endAngle >= startAngle) {
                                        endAngle - startAngle
                                    } else {
                                        endAngle - startAngle + 360 // 360도를 더해 시계 방향으로
                                    }

                                    val strokeWidth = 7.5.dp.toPx()
                                    val radius = (100.dp.toPx() - strokeWidth) / 2

                                    val centerX = size.width / 2f
                                    val centerY = radius + strokeWidth / 2

                                    // 배경
                                    drawCircle(
                                        color = Color(0xFF0A0A0A),
                                        radius = 50.dp.toPx()
                                    )
                                    // 게이지 배경
                                    drawArc(
                                        color = Color(0xFF353535),
                                        startAngle = 0f,
                                        sweepAngle = 360f,
                                        useCenter = false,
                                        style = Stroke(width = strokeWidth),
                                        topLeft = Offset(centerX - radius, centerY - radius),
                                        size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2)
                                    )
                                    // 게이지 값
                                    drawArc(
                                        color = transportationColor,
                                        startAngle = startAngle,
                                        sweepAngle = sweepAngle,
                                        useCenter = false,
                                        style = Stroke(width = strokeWidth),
                                        topLeft = Offset(centerX - radius, centerY - radius),
                                        size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2)
                                    )
                                }
                                //endregion
                                //region 원 그래프 내 시간 텍스트
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .wrapContentWidth(align = Alignment.CenterHorizontally)
                                        .wrapContentHeight(align = Alignment.CenterVertically)
                                ) {
                                    //region 변수
                                    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                                    val duration = endTime.time - startTime.time

                                    val hours = TimeUnit.MILLISECONDS.toHours(duration)
                                    val minutes = TimeUnit.MILLISECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(hours)
                                    val annotatedTimeString = buildAnnotatedString {
                                        withStyle(SpanStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold)) {
                                            append(hours.toString())
                                        }
                                        withStyle(SpanStyle(fontSize = 10.sp)) {
                                            append("시간 ")
                                        }
                                        withStyle(SpanStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold)) {
                                            append(minutes.toString())
                                        }
                                        withStyle(SpanStyle(fontSize = 10.sp)) {
                                            append("분")
                                        }
                                    }
                                    //endregion

                                    //region 소요 시간
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        text = annotatedTimeString,
                                        fontFamily = lineSeedKr,
                                        color = Color(0xFFFFFFFF),
                                        textAlign = TextAlign.Center
                                    )
                                    //endregion
                                    //region 시작 - 끝
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        text = "${timeFormat.format(startTime)} - ${timeFormat.format(endTime)}",
                                        fontFamily = lineSeedKr,
                                        fontSize = 11.sp,
                                        textAlign = TextAlign.Center,
                                        color = transportationColor
                                    )
                                    //endregion
                                }
                                //endregion
                            }
                        }
                        RecordDialogContainer(
                            title = "속도"
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                            ) {
                                //region 원 그래프
                                Canvas(
                                    modifier = Modifier
                                        .size(100.dp)
                                ) {
                                    val startAngle = 225f - 90f
                                    val sweepAngle = (dialogData.averageSpeed / dialogData.topSpeed) * 270

                                    val strokeWidth = 7.5.dp.toPx()
                                    val radius = (100.dp.toPx() - strokeWidth) / 2

                                    val centerX = size.width / 2f
                                    val centerY = radius + strokeWidth / 2

                                    // 배경
                                    drawCircle(
                                        color = Color(0xFF0A0A0A),
                                        radius = 50.dp.toPx()
                                    )
                                    // 게이지 배경
                                    drawArc(
                                        color = Color(0xFFF44242),
                                        startAngle = startAngle,
                                        sweepAngle = 270f,
                                        useCenter = false,
                                        style = Stroke(width = strokeWidth),
                                        topLeft = Offset(centerX - radius, centerY - radius),
                                        size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2)
                                    )
                                    // 값
                                    drawArc(
                                        color = transportationColor,
                                        startAngle = startAngle,
                                        sweepAngle = sweepAngle,
                                        useCenter = false,
                                        style = Stroke(width = strokeWidth),
                                        topLeft = Offset(centerX - radius, centerY - radius),
                                        size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2)
                                    )
                                }
                                //endregion
                                //region 원 그래프 내 속도 텍스트
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .wrapContentWidth(align = Alignment.CenterHorizontally)
                                        .wrapContentHeight(align = Alignment.CenterVertically),
                                    verticalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    val annotatedSpeedString = buildAnnotatedString {
                                        withStyle(SpanStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold)) {
                                            append(dialogData.averageSpeed.toSpeedUnit(prefManager.speedUnit).cutForSpeed(0).toInt().toString())
                                        }
                                        withStyle(SpanStyle(fontSize = 12.sp)) {
                                            append(" ${prefManager.speedUnit}\n")
                                        }
                                        withStyle(SpanStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold)) {
                                            append(dialogData.topSpeed.toSpeedUnit(prefManager.speedUnit).cutForSpeed(0).toInt().toString())
                                        }
                                        withStyle(SpanStyle(fontSize = 12.sp)) {
                                            append(" ${prefManager.speedUnit}")
                                        }
                                    }
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        textAlign = TextAlign.Center,
                                        text = "표정/최고속도",
                                        fontFamily = lineSeedKr,
                                        fontSize = 10.sp
                                    )
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        text = annotatedSpeedString,
                                        fontFamily = lineSeedKr,
                                        color = Color(0xFFFFFFFF),
                                        textAlign = TextAlign.Center,
                                        lineHeight = 18.sp
                                    )
                                }
                                //endregion
                            }
                        }
                    }
                    //endregion
                    //region 기타 정보
                    Column(
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        RecordOtherInfo(
                            name = "교통수단",
                            value = stringResource(transportation.label),
                            icon = transportation.icon,
                            iconColor = transportationColor
                        )
                        RecordOtherInfo(
                            name = "이동 거리",
                            value = "${(dialogData.distance/1000).cutForSpeed(1)} km"
                        )
                        RecordOtherInfo(
                            name = "요금 체계",
                            value = dialogData.fareCalcType
                        )
                        val calendar = Calendar.getInstance()
                        calendar.time = dialogData.startTime
                        val annotatedStartTime = buildAnnotatedString {
                            withStyle(SpanStyle(fontSize = 12.sp)) {
                                append("${calendar.get(Calendar.YEAR)}년 ")
                            }
                            withStyle(SpanStyle(fontSize = 15.sp)) {
                                append("${calendar.get(Calendar.MONTH)+1}")
                            }
                            withStyle(SpanStyle(fontSize = 12.sp)) {
                                append("월 ")
                            }
                            withStyle(SpanStyle(fontSize = 15.sp)) {
                                append("${calendar.get(Calendar.DAY_OF_MONTH)}")
                            }
                            withStyle(SpanStyle(fontSize = 12.sp)) {
                                append("일\n")
                            }
                            withStyle(SpanStyle(fontSize = 15.sp)) {
                                append("${calendar.get(Calendar.HOUR)}")
                            }
                            withStyle(SpanStyle(fontSize = 12.sp)) {
                                append("시 ")
                            }
                            withStyle(SpanStyle(fontSize = 15.sp)) {
                                append("${calendar.get(Calendar.MINUTE)}")
                            }
                            withStyle(SpanStyle(fontSize = 12.sp)) {
                                append("분 ")
                            }
                            withStyle(SpanStyle(fontSize = 15.sp)) {
                                append("${calendar.get(Calendar.SECOND)}")
                            }
                            withStyle(SpanStyle(fontSize = 12.sp)) {
                                append("초")
                            }
                        }
                        RecordOtherInfo(
                            name = "출발 시간",
                            annotatedValue = annotatedStartTime
                        )
                        calendar.time = dialogData.endTime
                        val annotatedEndTime = buildAnnotatedString {
                            withStyle(SpanStyle(fontSize = 12.sp)) {
                                append("${calendar.get(Calendar.YEAR)}년 ")
                            }
                            withStyle(SpanStyle(fontSize = 15.sp)) {
                                append("${calendar.get(Calendar.MONTH)+1}")
                            }
                            withStyle(SpanStyle(fontSize = 12.sp)) {
                                append("월 ")
                            }
                            withStyle(SpanStyle(fontSize = 15.sp)) {
                                append("${calendar.get(Calendar.DAY_OF_MONTH)}")
                            }
                            withStyle(SpanStyle(fontSize = 12.sp)) {
                                append("일\n")
                            }
                            withStyle(SpanStyle(fontSize = 15.sp)) {
                                append("${calendar.get(Calendar.HOUR)}")
                            }
                            withStyle(SpanStyle(fontSize = 12.sp)) {
                                append("시 ")
                            }
                            withStyle(SpanStyle(fontSize = 15.sp)) {
                                append("${calendar.get(Calendar.MINUTE)}")
                            }
                            withStyle(SpanStyle(fontSize = 12.sp)) {
                                append("분 ")
                            }
                            withStyle(SpanStyle(fontSize = 15.sp)) {
                                append("${calendar.get(Calendar.SECOND)}")
                            }
                            withStyle(SpanStyle(fontSize = 12.sp)) {
                                append("초")
                            }
                        }
                        RecordOtherInfo(
                            name = "도착 시간",
                            annotatedValue = annotatedEndTime
                        )
                    }
                    //endregion
                }
            }
        }
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