package kr.musekee.faremeter.components.main

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.musekee.faremeter.screens.bottomBorder
import kr.musekee.faremeter.ui.theme.lineSeedKr

@Composable
fun SettingCategory(icon: Int, color: Color, label: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .bottomBorder(1.dp, Color(0xFF777777)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .size(60.dp)
                .padding(10.dp),
            painter = painterResource(
                id = icon
            ),
            contentDescription = label,
            tint = color
        )
        Text(
            text = label,
            fontFamily = lineSeedKr,
            fontSize = 25.sp,
            textAlign = TextAlign.Center
        )
    }
}