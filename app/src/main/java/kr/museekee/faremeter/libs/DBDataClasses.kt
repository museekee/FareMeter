package kr.museekee.faremeter.libs

data class RecordData(
    val id: String, // 8fdehg943wh
    val fareCalcType: String, // 서울, 경기, 특실, 일반실, 우등실
    val transportation: String, // 교통수단 ID
    val endTime: Long, // 끝시간
    val fare: Int, // 운임
    val distance: Double, // 거리 m
)

data class DrivingData(
    val id: String,
    val latitude: Double, // 37.00001
    val longitude: Double, // 130.75745
    val speed: Double, // m/s
    val time: Long // ms
)