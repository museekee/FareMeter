package kr.musekee.faremeter.libs

import java.util.Date

data class RecordData(
    val _id: Int, // 123
    val fareCalcType: String, // 서울, 경기, 특실, 일반실, 우등실
    val transportation: String, // 교통수단 ID
    val startTime: Date, // 시작 시간
    val endTime: Date, // 끝 시간
    val fare: Int, // 운임
    val averageSpeed: Float, // 평균 속도 m/s
    val topSpeed: Float, // 최고 속도 m/s
    val distance: Double, // 거리 m
    val latitudes: List<Double>, // 위도 리스트
    val longitudes: List<Double> // 경도 리스트
)