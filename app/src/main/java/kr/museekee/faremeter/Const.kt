package kr.museekee.faremeter

import android.os.Environment

const val SELECT_TRANSPORTATION = "SELECT_TRANSPORTATION"
const val SETTINGS = "SETTINGS"
const val RESULTS = "RESULTS"
const val TAXI = "TAXI"
const val BUS = "BUS"
const val ACTION_STOP = "${BuildConfig.APPLICATION_ID}.stop"
const val NOTI_CHANNEL = "noti_channel"

const val KAKAO_TEMPLATE_ID = 113715L

val dataPath = "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath}/meterData"