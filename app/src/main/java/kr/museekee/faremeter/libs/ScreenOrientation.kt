package kr.museekee.faremeter.libs

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo

fun SetPortrait(context: Context) {
    (context as? Activity)?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
}
fun SetLandscape(context: Context) {
    (context as? Activity)?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
}