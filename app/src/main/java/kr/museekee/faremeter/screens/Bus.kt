package kr.museekee.faremeter.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import kr.museekee.faremeter.libs.SetLandscape

@Composable
fun Bus() {
    SetLandscape(LocalContext.current)
    Text(text = "버스")
}