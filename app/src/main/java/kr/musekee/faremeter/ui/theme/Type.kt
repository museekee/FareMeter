package kr.musekee.faremeter.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kr.musekee.faremeter.R

val lineSeedKr = FontFamily(
    Font(R.font.line_seed_kr_r, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.line_seed_kr_b, FontWeight.Bold, FontStyle.Normal),
)
val nanumGothic = FontFamily(
    Font(R.font.nanum_gothic_r, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.nanum_gothic_b, FontWeight.Bold, FontStyle.Normal),
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)