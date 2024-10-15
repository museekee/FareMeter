package kr.museekee.faremeter.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import kr.museekee.faremeter.ui.theme.FareMeterTheme

class UnknownActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent { 
            FareMeterTheme {
                Column {
                    Text(text = "헉! 여기 들어오면 문제 있는건데")
                    Text(text = "앱을 지웠다가 다시 깔아보실래요?")
                    Text(text = "다시 깔면 주행 기록과 설정은 삭제될거예요.")
                }
            }
        }
    }
}