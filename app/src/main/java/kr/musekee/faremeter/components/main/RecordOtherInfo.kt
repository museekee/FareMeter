package kr.musekee.faremeter.components.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.musekee.faremeter.ui.theme.lineSeedKr

@Composable
fun RecordOtherInfo(
    name: String = "알 수 없음",
    annotatedName: AnnotatedString? = null,
    value: String = "알 수 없음",
    annotatedValue: AnnotatedString? = null,
    icon: Int? = null,
    iconColor: Color = Color(0xFFFFFFFF)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (annotatedName != null)
            Text(
                modifier = Modifier
                    .weight(1f),
                text = annotatedName,
                fontSize = 15.sp,
                fontFamily = lineSeedKr
            )
        else
            Text(
                modifier = Modifier
                    .weight(1f),
                text = name,
                fontSize = 15.sp,
                fontFamily = lineSeedKr
            )
        Row(
            modifier = Modifier
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            if (icon != null)
                Icon(
                    modifier = Modifier
                        .size(20.dp),
                    painter = painterResource(id = icon),
                    contentDescription = value,
                    tint = iconColor
                )
            if (annotatedValue != null)
                Text(
                    text = annotatedValue,
                    fontSize = 15.sp,
                    fontFamily = lineSeedKr,
                    color = Color(0xFFEEEEEE)
                )
            else
                Text(
                    text = value,
                    fontSize = 15.sp,
                    fontFamily = lineSeedKr,
                    color = Color(0xFFEEEEEE)
                )
        }
    }
}