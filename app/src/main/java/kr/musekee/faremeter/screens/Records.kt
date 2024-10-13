package kr.musekee.faremeter.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.musekee.faremeter.R
import kr.musekee.faremeter.components.RecordDialog
import kr.musekee.faremeter.components.main.ImportRecordDialog
import kr.musekee.faremeter.components.main.MainTitle
import kr.musekee.faremeter.components.main.RecordItem
import kr.musekee.faremeter.datas.transportations
import kr.musekee.faremeter.libs.DatabaseHelper
import kr.musekee.faremeter.libs.RecordDao
import kr.musekee.faremeter.libs.RecordData
import kr.musekee.faremeter.libs.bottomBorder
import kr.musekee.faremeter.ui.theme.lineSeedKr
import kr.musekee.faremeter.ui.theme.nanumGothic

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Records() {
    val scrollState = rememberScrollState()
    var detailDialogEnabled by remember { mutableStateOf(false) }
    var importDialogEnabled by remember { mutableStateOf(false) }
    var dialogData by remember { mutableStateOf(RecordData("", "서울특별시", "TAXI", 0, 10000, 50.51)) }
    val recordDao = RecordDao(DatabaseHelper(LocalContext.current))
    var isTransportationPopup by remember { mutableStateOf(false) }
    var limit by remember { mutableIntStateOf(0) }
    var transportation: String? by remember { mutableStateOf(null) } // 전체 보기는 null
    var dateOrder by remember { mutableStateOf("DESC") }

    Column {
        MainTitle(
            title = stringResource(id = R.string.Results)
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .bottomBorder(1.dp, Color(0xFF777777)),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
            onClick = {
                importDialogEnabled = true
            }
        ) {
            Text(
                color = Color(0xFFBBBBBB),
                text = "주행 기록 불러오기"
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp)
                .bottomBorder(1.dp, Color(0xFF777777))
                .padding(horizontal = 10.dp)
        ) { // 여기서 정렬 방식 선택, #dp tdp fWeight ddp
            Text(
                modifier = Modifier
                    .width(55.dp)
                    .fillMaxHeight()
                    .wrapContentHeight(align = Alignment.CenterVertically),
                text = "#",
                fontFamily = nanumGothic,
                fontSize = 18.sp,
                textAlign = TextAlign.Right
            )
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .wrapContentHeight(align = Alignment.CenterVertically)
                    .width(90.dp)
                    .clickable {
                        isTransportationPopup = true
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    space = 5.dp,
                    alignment = Alignment.CenterHorizontally
                )
            ) {
                Text(
                    text = "교통수단",
                    fontFamily = lineSeedKr,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
                Icon(
                    modifier = Modifier
                        .size(10.dp)
                        .rotate(90f),
                    painter = painterResource(id = R.drawable.ic_arrow),
                    contentDescription = "교통수단 선택"
                )
            }
            Text(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .wrapContentHeight(align = Alignment.CenterVertically),
                text = "운임",
                fontFamily = lineSeedKr,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .wrapContentHeight(align = Alignment.CenterVertically)
                    .width(70.dp)
                    .clickable {
                        dateOrder = if (dateOrder == "DESC")
                            "ASC"
                        else
                            "DESC"
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    space = 5.dp,
                    alignment = Alignment.End
                )
            ) {
                Text(
                    text = "날짜",
                    fontFamily = lineSeedKr,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Right,
                    color = Color(0xFFBBBBBB)
                )
                Icon(
                    modifier = Modifier
                        .size(10.dp)
                        .rotate(if (dateOrder == "DESC") 90f else 270f),
                    painter = painterResource(id = R.drawable.ic_arrow),
                    tint = Color(0xFFBBBBBB),
                    contentDescription = "날짜 정렬"
                )
            }
        }
        Column( // lazycolumn으로 바꾸고
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(scrollState)
        ) {
            // #에서 limit 설정하기
            recordDao.getAllData()
                .limit(limit)
                .transportation(transportation)
                .orderBy("END_TIME", dateOrder)
                .execute().mapIndexed { index, it ->
                RecordItem(index, it.transportation, it.fare, it.distance, it.endTime) {
                    Log.d("endtime", it.endTime.toString())
                    dialogData = it
//                    drivingDataDao.AVGTEST(it.id)
                    detailDialogEnabled = true
                }
            }
        }
    }
    if (isTransportationPopup) {
        ModalBottomSheet(
            onDismissRequest = {
                isTransportationPopup = false
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clickable {
                            transportation = null
                            isTransportationPopup = false
                        }
                        .background(if (transportation == null) Color(0x88000000) else Color.Transparent),
                    horizontalArrangement = Arrangement.spacedBy(
                        space = 10.dp,
                        alignment = Alignment.CenterHorizontally
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier
                            .size(30.dp),
                        painter = painterResource(
                            id = R.drawable.ic_all_transportations
                        ),
                        contentDescription = "전체"
                    )
                    Text(
                        text = "전체",
                        fontFamily = lineSeedKr,
                        fontSize = 25.sp
                    )
                }
                transportations.map {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clickable {
                                transportation = it.id
                                isTransportationPopup = false
                            }
                            .background(if (transportation == it.id) Color(0x88000000) else Color.Transparent),
                        horizontalArrangement = Arrangement.spacedBy(
                            space = 10.dp,
                            alignment = Alignment.CenterHorizontally
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(30.dp),
                            painter = painterResource(
                                id = it.icon
                            ),
                            contentDescription = stringResource(
                                id = it.label
                            ),
                            tint = it.color
                        )
                        Text(
                            text = stringResource(
                                id = it.label
                            ),
                            fontFamily = lineSeedKr,
                            fontSize = 25.sp
                        )
                    }
                }
            }
        }
    }

    if (importDialogEnabled) {
        ImportRecordDialog(
            onDismiss = {
                importDialogEnabled = false
            }
        )
    }

    if (detailDialogEnabled) {
        RecordDialog(
            id = dialogData.id,
            onDismiss = { detailDialogEnabled = false },
            onDeleteButtonClick = {
                recordDao.deleteData(dialogData.id)
            },
            onCloseBtnClick = {}
        )
    }
}