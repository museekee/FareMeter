package kr.museekee.faremeter.components

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.kakao.vectormap.KakaoMapSdk
import kr.museekee.faremeter.BuildConfig
import kr.museekee.faremeter.components.main.RecordDialogContainer
import kr.museekee.faremeter.components.main.RecordOtherInfo
import kr.museekee.faremeter.components.main.RouteKakaoMap
import kr.museekee.faremeter.dataPath
import kr.museekee.faremeter.datas.getTransportationById
import kr.museekee.faremeter.libs.DatabaseHelper
import kr.museekee.faremeter.libs.DrivingData
import kr.museekee.faremeter.libs.DrivingDataDao
import kr.museekee.faremeter.libs.PrefManager
import kr.museekee.faremeter.libs.RecordDao
import kr.museekee.faremeter.libs.cutForDecimal
import kr.museekee.faremeter.libs.toSpeedUnit
import kr.museekee.faremeter.libs.wonFormat
import kr.museekee.faremeter.ui.theme.lineSeedKr
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

@Composable
fun RecordDialog(
    id: String,
    onCloseBtnClick: () -> Unit,
    onDeleteButtonClick: () -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val prefManager = PrefManager(context)
    val dbHelper = DatabaseHelper(context)
    val recordDao = RecordDao(dbHelper)
    val drivingDataDao = DrivingDataDao(dbHelper)
    val rData = recordDao.getAllData().id(id).execute()[0]
    val lData = drivingDataDao.getData(id)
    val topSpeed = drivingDataDao.getTopSpeed(id)
    val averageSpeed = drivingDataDao.getAverageSpeed(id)

    Dialog(
        onDismissRequest = {
            KakaoMapSdk.init(context, BuildConfig.KAKAO_API_KEY) // 초기화 해야 현재 루트가 사라짐
            onDismiss()
        },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        val transportation = getTransportationById(rData.transportation)
        val transportationColor = transportation.color
        val startTime = Date(lData[0].time)
        val endTime = Date(lData.last().time)
        var memo by remember { mutableStateOf(rData.memo) }
        var isMapMoving by remember { mutableStateOf(false) }
        var showRemoveDialog by remember { mutableStateOf(false) }

        Card(
            modifier = Modifier
                .width(375.dp)
                .height(600.dp)
                .padding(10.dp)
                .verticalScroll(
                    rememberScrollState(),
                    enabled = !isMapMoving
                ),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF202020)
            ),
            shape = RoundedCornerShape(25.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "${rData.fare.wonFormat()}원",
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
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    val graphSize = 130.dp
                    RecordDialogContainer(
                        title = "시간"
                    ) {
                        Box(
                            modifier = Modifier
                                .size(graphSize)
                        ) {
                            //region 원 그래프
                            Canvas(
                                modifier = Modifier
                                    .size(graphSize)
                            ) {
                                // 1분 당 0.5도
                                val startAngle = (convertToMinutes(startTime) / 2f) - 90 // 기본 0도는 3시 방향인 듯...
                                val endAngle = (convertToMinutes(endTime) / 2f) - 90
                                val sweepAngle = if (endAngle >= startAngle) {
                                    endAngle - startAngle
                                } else {
                                    endAngle - startAngle + 360 // 360도를 더해 시계 방향으로
                                }

                                val isFullSizeValue = (endTime.time - startTime.time) / 1000 >= 43200

                                val strokeWidth = 7.5.dp.toPx()
                                val radius = (graphSize.toPx() - strokeWidth) / 2

                                val centerX = size.width / 2f
                                val centerY = radius + strokeWidth / 2

                                // 배경
                                drawCircle(
                                    color = Color(0xFF0A0A0A),
                                    radius = (graphSize/2).toPx()
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
                                if (!isFullSizeValue)
                                    drawArc(
                                        color = transportationColor,
                                        startAngle = startAngle,
                                        sweepAngle = sweepAngle,
                                        useCenter = false,
                                        style = Stroke(width = strokeWidth),
                                        topLeft = Offset(centerX - radius, centerY - radius),
                                        size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2)
                                    )
                                else
                                    drawArc(
                                        color = transportationColor,
                                        startAngle = 0f,
                                        sweepAngle = 360f,
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
                                    withStyle(SpanStyle(fontSize = 27.sp, fontWeight = FontWeight.Bold)) {
                                        append(hours.toString())
                                    }
                                    withStyle(SpanStyle(fontSize = 13.sp)) {
                                        append("시간 ")
                                    }
                                    withStyle(SpanStyle(fontSize = 27.sp, fontWeight = FontWeight.Bold)) {
                                        append(minutes.toString())
                                    }
                                    withStyle(SpanStyle(fontSize = 13.sp)) {
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
                                    fontSize = 13.sp,
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
                                .size(graphSize)
                        ) {
                            //region 원 그래프
                            Canvas(
                                modifier = Modifier
                                    .size(graphSize)
                            ) {
                                val startAngle = 225f - 90f
                                val sweepAngle = (averageSpeed / topSpeed) * 270

                                val strokeWidth = 7.5.dp.toPx()
                                val radius = (graphSize.toPx() - strokeWidth) / 2

                                val centerX = size.width / 2f
                                val centerY = radius + strokeWidth / 2

                                // 배경
                                drawCircle(
                                    color = Color(0xFF0A0A0A),
                                    radius = 65.dp.toPx()
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
                                    sweepAngle = sweepAngle.toFloat(),
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
                                    withStyle(style = ParagraphStyle(lineHeight = 25.sp)) {
                                        withStyle(
                                            SpanStyle(
                                                fontSize = 22.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        ) {
                                            append(
                                                averageSpeed.toSpeedUnit(prefManager.speedUnit)
                                                    .cutForDecimal(0).toInt().toString()
                                            )
                                        }
                                        withStyle(SpanStyle(fontSize = 15.sp)) {
                                            append(" ${prefManager.speedUnit}\n")
                                        }
                                        withStyle(
                                            SpanStyle(
                                                fontSize = 22.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        ) {
                                            append(
                                                topSpeed.toSpeedUnit(prefManager.speedUnit)
                                                    .cutForDecimal(0).toInt().toString()
                                            )
                                        }
                                        withStyle(SpanStyle(fontSize = 15.sp)) {
                                            append(" ${prefManager.speedUnit}")
                                        }
                                    }
                                }
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    text = "표정/최고속도",
                                    fontFamily = lineSeedKr,
                                    fontSize = 13.sp
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
                        value = "${(rData.distance/1000).cutForDecimal(1)} km"
                    )
                    RecordOtherInfo(
                        name = "요금 체계",
                        value = rData.fareCalcType
                    )
                    val calendar = Calendar.getInstance()
                    calendar.time = startTime
                    val annotatedStartTime = buildAnnotatedString {
                        withStyle(SpanStyle(fontSize = 12.sp)) {
                            append("${calendar.get(Calendar.YEAR)}년 ")
                        }
                        withStyle(SpanStyle(fontSize = 17.sp)) {
                            append("${calendar.get(Calendar.MONTH)+1}")
                        }
                        withStyle(SpanStyle(fontSize = 12.sp)) {
                            append("월 ")
                        }
                        withStyle(SpanStyle(fontSize = 17.sp)) {
                            append("${calendar.get(Calendar.DAY_OF_MONTH)}")
                        }
                        withStyle(SpanStyle(fontSize = 12.sp)) {
                            append("일\n")
                        }
                        withStyle(SpanStyle(fontSize = 17.sp)) {
                            append("${calendar.get(Calendar.HOUR)}")
                        }
                        withStyle(SpanStyle(fontSize = 12.sp)) {
                            append("시 ")
                        }
                        withStyle(SpanStyle(fontSize = 17.sp)) {
                            append("${calendar.get(Calendar.MINUTE)}")
                        }
                        withStyle(SpanStyle(fontSize = 12.sp)) {
                            append("분 ")
                        }
                        withStyle(SpanStyle(fontSize = 17.sp)) {
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
                    calendar.time = endTime
                    val annotatedEndTime = buildAnnotatedString {
                        withStyle(SpanStyle(fontSize = 12.sp)) {
                            append("${calendar.get(Calendar.YEAR)}년 ")
                        }
                        withStyle(SpanStyle(fontSize = 17.sp)) {
                            append("${calendar.get(Calendar.MONTH)+1}")
                        }
                        withStyle(SpanStyle(fontSize = 12.sp)) {
                            append("월 ")
                        }
                        withStyle(SpanStyle(fontSize = 17.sp)) {
                            append("${calendar.get(Calendar.DAY_OF_MONTH)}")
                        }
                        withStyle(SpanStyle(fontSize = 12.sp)) {
                            append("일\n")
                        }
                        withStyle(SpanStyle(fontSize = 17.sp)) {
                            append("${calendar.get(Calendar.HOUR)}")
                        }
                        withStyle(SpanStyle(fontSize = 12.sp)) {
                            append("시 ")
                        }
                        withStyle(SpanStyle(fontSize = 17.sp)) {
                            append("${calendar.get(Calendar.MINUTE)}")
                        }
                        withStyle(SpanStyle(fontSize = 12.sp)) {
                            append("분 ")
                        }
                        withStyle(SpanStyle(fontSize = 17.sp)) {
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

                    val noGPSTimePos = mutableListOf<Pair<DrivingData, DrivingData>>()
                    for (i in 1 until lData.size) {
                        if (lData[i].time - lData[i - 1].time > 5000L) {
                            noGPSTimePos.add(Pair(lData[i - 1], lData[i]))
                        }
                    }

                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(50.dp)
                            .border(1.dp, Color(0xFF151515), shape = RoundedCornerShape(20.dp)),
                        value = memo,
                        placeholder = { Text(text = "메모를 입력해주세요") },
                        onValueChange = {
                            Log.d("RecordDialog", "메모 텍스트 필드 변화: $it")
                            memo = it
                        }
                    )

                    RouteKakaoMap(
                        modifier = Modifier.onPointerInteractionStartEnd(
                            onPointerStart = {
                                isMapMoving = true },
                            onPointerEnd = {
                                isMapMoving = false
                            }
                        ),
                        latLng = lData
                    )
                }
                //endregion
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(
                        modifier = Modifier
                            .width(80.dp)
                            .height(45.dp)
                            .border(
                                width = 1.dp,
                                color = Color(0xFFD32F2F),
                                shape = RoundedCornerShape(15.dp)
                            )
                            .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                        shape = RoundedCornerShape(15.dp),
                        onClick = {
                            showRemoveDialog = true
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color(0xFFD32F2F),
                            disabledContainerColor = Color.Gray,
                            disabledContentColor = Color.LightGray,
                        )
                    ) {
                        Text(
                            text = "삭제"
                        )
                    }
                    Button(
                        modifier = Modifier
                            .width(95.dp)
                            .height(45.dp)
                            .border(
                                width = 1.dp,
                                color = Color(0xFF888888),
                                shape = RoundedCornerShape(15.dp)
                            )
                            .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                        shape = RoundedCornerShape(15.dp),
                        onClick = {
                            Log.d("RecordDialog", dataPath)
                            val file = File("$dataPath/$id.meter")
                            if (!file.exists())
                                file.createNewFile()
                            val writer = FileWriter(file, false)
                            writer.write("${recordDao.export(id)}\n")
                            writer.write(drivingDataDao.export(id))
                            writer.flush()
                            writer.close()
                            Log.d("RecordDialog", "파일 씀")
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color(0xFF888888),
                            disabledContainerColor = Color.Gray,
                            disabledContentColor = Color.LightGray,
                        )
                    ) {
                        Text(
                            text = "내보내기"
                        )
                    }
                    Button(
                        modifier = Modifier
                            .width(80.dp)
                            .height(45.dp)
                            .border(
                                width = 4.dp,
                                color = Color(0xFF0091EA),
                                shape = RoundedCornerShape(15.dp)
                            )
                            .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                        shape = RoundedCornerShape(15.dp),
                        onClick = {
                            onCloseBtnClick()
                            recordDao.updateMemo(rData.id, memo)
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF0091EA),
                            contentColor = Color(0xFFFFFFFF),
                            disabledContainerColor = Color.Gray,
                            disabledContentColor = Color.LightGray,
                        )
                    ) {
                        Text(
                            text = "확인"
                        )
                    }
                }
            }
        }

        if (showRemoveDialog)
            YesNoDialog(
                title = "정말로 기록을 삭제하시겠습니까?",
                description = "삭제 후에는 복구할 수 없습니다",
                yesLabel = "삭제",
                noLabel = "취소",
                yesColor = Color(0xFFD32F2F),
                onClickYes = {
                    onDeleteButtonClick()
                    onDismiss()
                },
                onDismiss = {
                    showRemoveDialog = false
                }
            )
    }
}
fun Modifier.onPointerInteractionStartEnd(
    onPointerStart: () -> Unit,
    onPointerEnd: () -> Unit,
) = this then pointerInput(onPointerStart, onPointerEnd) {
    awaitEachGesture {
        awaitFirstDown(requireUnconsumed = false)
        onPointerStart()
        do {
            val event = awaitPointerEvent()
        } while (event.changes.any { it.pressed })
        onPointerEnd()
    }
}

fun convertToMinutes(date: Date): Int {
    val calendar = Calendar.getInstance()
    calendar.time = date

    val hour = calendar.get(Calendar.HOUR_OF_DAY) % 12
    val minute = calendar.get(Calendar.MINUTE)

    return hour * 60 + minute
}