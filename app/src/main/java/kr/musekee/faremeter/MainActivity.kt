package kr.musekee.faremeter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import kr.musekee.faremeter.screens.BottomNavigation
import kr.musekee.faremeter.screens.NavigationHost
import kr.musekee.faremeter.ui.theme.FareMeterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val navController = rememberNavController()

            FareMeterTheme {
                Scaffold(
                    topBar = {},
                    bottomBar = {
                        BottomNavigation(navController = navController)
                    }
                ) {
                    Box(modifier = Modifier.padding(it)){
                        NavigationHost(navController = navController)
                    }
                }
            }
        }
    }
}
