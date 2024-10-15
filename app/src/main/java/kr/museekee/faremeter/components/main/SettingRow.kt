package kr.museekee.faremeter.components.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kr.museekee.faremeter.libs.bottomBorder

@Composable
fun SettingRow(
    icon: Int,
    color: Color,
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .bottomBorder(1.dp, Color(0xFF777777))
    ) {
        SettingCategory(
            icon = icon,
            color = color,
            label = title
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 25.dp)
                .padding(vertical = 5.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            content()
        }
    }
}