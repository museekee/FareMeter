package kr.musekee.faremeter.components.main

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.musekee.faremeter.screens.bottomBorder
import kr.musekee.faremeter.ui.theme.lineSeedKr

@Composable
fun MainTitle(title: String) {
    Text( // 제목
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .bottomBorder(1.dp, Color(0xFF777777))
            .wrapContentHeight(align = Alignment.CenterVertically),
        text = title,
        fontWeight = FontWeight.Bold,
        fontFamily = lineSeedKr,
        fontSize = 17.sp,
        textAlign = TextAlign.Center
    )
}