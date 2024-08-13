package kr.musekee.faremeter

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import androidx.preference.PreferenceManager
import kr.musekee.faremeter.datas.taxi
import kr.musekee.faremeter.datas.transportations
import kr.musekee.faremeter.screens.BottomNavigation
import kr.musekee.faremeter.screens.NavigationHost
import kr.musekee.faremeter.ui.theme.FareMeterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val edit = pref.edit()
        if (pref.getString("pref_transportation", null) == null)
            edit.putString("pref_transportation", taxi.id)
        for (transportation in transportations)
            if (pref.getString("pref_${transportation.id}_calcType", null) == null)
                edit.putString("pref_${transportation.id}_calcType", transportation.calcTypes[0])
        edit.apply()
        setContent {
            val navController = rememberNavController()
//            val permissions = arrayOf(
//                Manifest.permission.ACCESS_COARSE_LOCATION,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            )
//
//            val launcherMultiplePermissions = rememberLauncherForActivityResult(
//                ActivityResultContracts.RequestMultiplePermissions()
//            ) { permissionsMap ->
//                val areGranted = permissionsMap.values.reduce { acc, next -> acc && next }
//                if (!areGranted)
//                    (permissions)
//            }
//            checkAndRequestPermissions(
//                this,
//                permissions,
//                launcherMultiplePermissions
//            )
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
    private fun checkAndRequestPermissions(
        context: Context,
        permissions: Array<String>,
        launcher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>,
    ) {
        /** 권한이 없는 경우 **/
        if (!permissions.all {
            ContextCompat.checkSelfPermission(
                context,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }) {
            launcher.launch(permissions)
        }
    }
}
