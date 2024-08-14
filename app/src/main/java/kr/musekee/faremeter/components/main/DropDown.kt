package kr.musekee.faremeter.components.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.musekee.faremeter.ui.theme.lineSeedKr

@Composable
fun SettingDropDown(icon: Int, color: Color, name: String, values: List<String>, defaultValue: String, onChange: (newValue: String) -> Unit) {
    var value by remember { mutableStateOf(defaultValue) }
    var isDropDownMenuExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
            .clickable { isDropDownMenuExpanded = true },
    ) {
        Icon(
            modifier = Modifier
                .size(65.dp)
                .padding(15.dp),
            painter = painterResource(
                id = icon
            ),
            contentDescription = name,
            tint = color
        )
        Column(
            modifier = Modifier
                .height(65.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterVertically)
        ) {
            Text(
                text = name,
                fontFamily = lineSeedKr,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                color = Color(0xFFFFFFFF)
            )
            Text(
                text = value,
                fontFamily = lineSeedKr,
                fontSize = 22.sp
            )
        }
    }

    DropdownMenu(
        modifier = Modifier
            .wrapContentSize(),
        expanded = isDropDownMenuExpanded,
        onDismissRequest = { isDropDownMenuExpanded = false }
    ) {
        values.map {
            DropdownMenuItem(
                modifier = Modifier
                    .background(if (it == value) Color.Cyan else Color.Unspecified),
                text = {
                    Text(
                        text = it
                    )
                },
                onClick = {
                    value = it
                    onChange(it)
                }
            )
        }
    }
}