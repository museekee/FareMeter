package kr.musekee.faremeter.components.main

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.musekee.faremeter.R
import kr.musekee.faremeter.screens.bottomBorder
import kr.musekee.faremeter.ui.theme.lineSeedKr
import kr.musekee.faremeter.ui.theme.nanumGothic
import java.util.Date
import java.util.concurrent.TimeUnit

@Composable
fun ResultItem(
    idx: Int,
    transportation: String,
    fare: Int,
    distance: Float,
    date: Date
) {
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .bottomBorder(1.dp, Color(0xFF777777))
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text( // 순번
            modifier = Modifier
                .fillMaxHeight()
                .wrapContentHeight(align = Alignment.CenterVertically),
            text = (idx+1).toString(),
            fontFamily = nanumGothic,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            textAlign = TextAlign.Center
        )
        Icon( // 아이콘
            modifier = Modifier
                .size(90.dp)
                .padding(17.dp),
            painter = painterResource(id = R.drawable.ic_taxi),
            contentDescription = "택시",
            tint = Color(0xFFFFDE4D)
        )
        Column( // 운임
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = "${fare}원",
                fontFamily = lineSeedKr,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp
            )
            Text(
                text = "$distance km",
                fontFamily = lineSeedKr,
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp
            )
        }
        Text( // 날짜
            modifier = Modifier
                .width(70.dp),
            text = "${-getDateDiff(Date(), date, TimeUnit.DAYS)}일 전",
            color = Color(0xFFBBBBBB),
            fontFamily = lineSeedKr,
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp,
            textAlign = TextAlign.Right
        )
    }
}

fun getDateDiff(date1: Date, date2: Date, timeUnit: TimeUnit): Long {
    val diffInMillies = date2.time - date1.time
    return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS)
}