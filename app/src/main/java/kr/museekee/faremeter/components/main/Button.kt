package kr.museekee.faremeter.components.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainButton(label: String, icon: Int, color: Color, enabled: Boolean = true, onClick: () -> Unit) {
    Button(
        modifier = Modifier
            .size(150.dp)
            .background(color, CircleShape)
            .padding(vertical = 5.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        enabled = enabled,
        onClick = onClick
    ) {
        Column {
            Icon(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                painter = painterResource(
                    id = icon
                ),
                contentDescription = label,
                tint = Color.Black
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                text = label
            )
        }
    }
}