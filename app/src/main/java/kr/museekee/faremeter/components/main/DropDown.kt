package kr.museekee.faremeter.components.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.museekee.faremeter.ui.theme.lineSeedKr

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingDropDown(
    icon: Int,
    color: Color,
    name: String,
    values: List<String>,
    defaultValue: String,
    dropDownTitle: String,
    onChange: (newValue: String) -> Unit
) {
    var value by remember { mutableStateOf(defaultValue) }
    var isPopup by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
            .clickable { isPopup = true },
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
    if (isPopup)
        ModalBottomSheet(
            onDismissRequest = {
                isPopup = false
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 25.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontFamily = lineSeedKr,
                    text = dropDownTitle
                )
                values.map {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 20.dp,
                            )
                            .background(
                                color = if (it == value) Color(0x88000000) else Color(0x00000000),
                                shape = RoundedCornerShape(15.dp)
                            )
                            .clickable {
                                value = it
                                onChange(it)
                                isPopup = false
                            }
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(10.dp),
                            text = it,
                            fontFamily = lineSeedKr,
                            fontSize = 20.sp,
                            fontWeight = if (it == value) FontWeight.Bold else FontWeight.Medium,
                            color = if (it == value) Color(0xFFFFFFFF) else Color.Unspecified
                        )
                    }
                }
            }
        }
}