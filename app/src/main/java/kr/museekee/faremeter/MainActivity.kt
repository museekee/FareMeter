package kr.museekee.faremeter

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
import com.kakao.vectormap.KakaoMapSdk
import kr.museekee.faremeter.datas.taxi
import kr.museekee.faremeter.datas.transportations
import kr.museekee.faremeter.datas.unknownTransportation
import kr.museekee.faremeter.libs.PrefManager
import kr.museekee.faremeter.screens.BottomNavigation
import kr.museekee.faremeter.screens.NavigationHost
import kr.museekee.faremeter.ui.theme.FareMeterTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

//        val dataDir = File(dataPath)
//        if (!dataDir.exists())
//            dataDir.mkdir()
        val prefManager = PrefManager(this)
        if (prefManager.transportation == unknownTransportation.id)
            prefManager.transportation = taxi.id
        for (transportation in transportations)
            if (prefManager.getCalcType(transportation.id) == "")
                prefManager.setCalcType(transportation.id, transportation.calcTypes[0])

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

        // DB EXPORT
//        val file = Environment.getExternalStorageDirectory()
//        val currentPath = "/data/data/kr.musekee.faremeter/databases/data.db"
//        val copyPath = "/Download/copydb_name.db"
//        val currentDB = File(currentPath)
//        val backupDB = File(file, copyPath)
//        if (currentDB.exists()) {
//            val src = FileInputStream(currentDB).channel
//            val dst = FileOutputStream(backupDB).channel
//            dst.transferFrom(src, 0, src.size())
//            src.close()
//            dst.close()
//        }
        KakaoMapSdk.init(this@MainActivity, BuildConfig.KAKAO_API_KEY)
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