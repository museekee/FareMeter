package kr.museekee.faremeter.components.main

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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.museekee.faremeter.datas.transportations
import kr.museekee.faremeter.datas.unknownTransportation
import kr.museekee.faremeter.libs.bottomBorder
import kr.museekee.faremeter.libs.cutForDecimal
import kr.museekee.faremeter.libs.wonFormat
import kr.museekee.faremeter.ui.theme.lineSeedKr
import kr.museekee.faremeter.ui.theme.nanumGothic
import java.util.concurrent.TimeUnit

@Composable
fun RecordItem(
    idx: Int,
    transportation: String,
    fare: Int,
    distance: Double,
    date: Long,
    onClick: () -> Unit
) {
    val transportationData = transportations.find { it.id == transportation } ?: unknownTransportation

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .bottomBorder(1.dp, Color(0xFF777777))
            .padding(horizontal = 10.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text( // 순번
            modifier = Modifier
                .width(55.dp)
                .fillMaxHeight()
                .wrapContentHeight(align = Alignment.CenterVertically),
            text = (idx+1).toString(),
            fontFamily = nanumGothic,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            textAlign = TextAlign.Right
        )
        Icon( // 아이콘
            modifier = Modifier
                .size(90.dp)
                .padding(17.dp),
            painter = painterResource(id = transportationData.icon),
            contentDescription = stringResource(transportationData.label),
            tint = transportationData.color
        )
        Column( // 운임
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = "${fare.wonFormat()}원",
                fontFamily = lineSeedKr,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp
            )
            Text(
                text = "${(distance/1000).cutForDecimal(1)} km",
                fontFamily = lineSeedKr,
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp
            )
        }
        Text( // 날짜
            modifier = Modifier
                .width(70.dp),
            text = "${-getDateDiff(System.currentTimeMillis(), date, TimeUnit.DAYS)}일 전",
            color = Color(0xFFBBBBBB),
            fontFamily = lineSeedKr,
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp,
            textAlign = TextAlign.Right
        )
    }
}

fun getDateDiff(date1: Long, date2: Long, timeUnit: TimeUnit): Long {
    val diffInMillis = date2 - date1
    return timeUnit.convert(diffInMillis, TimeUnit.MILLISECONDS)
}