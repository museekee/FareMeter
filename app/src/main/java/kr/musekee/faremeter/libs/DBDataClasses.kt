package kr.musekee.faremeter.libs

data class RecordData(
    val _id: Int, // 123
    val fareCalcType: String, // 서울, 경기, 특실, 일반실, 우등실
    val transportation: String, // 교통수단 ID
    val fare: Int, // 운임
    val averageSpeed: Float, // 평균 속도 m/s
    val topSpeed: Float, // 최고 속도 m/s
    val distance: Double, // 거리 m
    val timePos: List<TimePosition>
)

data class TimePosition(
    val latitude: Double, // 37.00001
    val longitude: Double, // 130.75745
    val time: Long // ms
)