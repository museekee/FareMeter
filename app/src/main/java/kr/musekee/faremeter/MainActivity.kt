package kr.musekee.faremeter

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
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
import kr.musekee.faremeter.libs.RecordSql
import kr.musekee.faremeter.screens.BottomNavigation
import kr.musekee.faremeter.screens.NavigationHost
import kr.musekee.faremeter.ui.theme.FareMeterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        // 아... setting을 위해서 싹 다 sql로 갈아야겠다...
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val edit = pref.edit()
        if (pref.getString("pref_transportation", null) == null)
            edit.putString("pref_transportation", taxi.id)
        if (pref.getString("pref_fareCalcType", null) == null)
            edit.putString("pref_fareCalcType", )

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
