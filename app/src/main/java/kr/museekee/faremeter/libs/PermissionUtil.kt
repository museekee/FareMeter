package kr.museekee.faremeter.libs

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.core.content.ContextCompat

object PermissionUtil {
    fun openAppInfo(context: Context) {
        Toast.makeText(
            context,
            "권한 없당",
            Toast.LENGTH_LONG
        ).show()
        val permissionIntent = Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            addCategory(Intent.CATEGORY_DEFAULT)
            data = Uri.parse("package:kr.musekee.faremeter")
        }
        context.startActivity(permissionIntent)
    }

    fun checkLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
}