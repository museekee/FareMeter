package kr.musekee.faremeter

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kr.musekee.faremeter.libs.LocationService
import kr.musekee.faremeter.libs.PermissionL.checkLocationPermission
import kr.musekee.faremeter.screens.BottomNavigation
import kr.musekee.faremeter.screens.NavigationHost
import kr.musekee.faremeter.ui.theme.FareMeterTheme

class MainActivity : ComponentActivity() {
    private var locationService: LocationService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        locationService = LocationService(this)

        setContent {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            var speed by remember { mutableFloatStateOf(0.0f) }
            LaunchedEffect(locationService?.getSpeed()) {
                speed = locationService!!.getSpeed()
            }

            FareMeterTheme {
                Scaffold(
                    topBar = {},
                    bottomBar = {
                        if (
                            navBackStackEntry?.destination?.route == SETTINGS ||
                            navBackStackEntry?.destination?.route == SELECT_TRANSPORTATION
                        ) BottomNavigation(navController = navController)
                    }
                ) {
                    Text(
                        text = speed.toString()
                    )
                    Box(modifier = Modifier.padding(it)){
                        NavigationHost(navController = navController)
                    }
                }
            }
        }
    }
}
