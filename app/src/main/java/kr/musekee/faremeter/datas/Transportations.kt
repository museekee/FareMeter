package kr.musekee.faremeter.datas

import androidx.compose.ui.graphics.Color
import kr.musekee.faremeter.R

/**
 * 모든 요금의 단위는 원 (￦)이고,
 * 거리의 단위는 미터 (m)이며,
 * 시간의 단위는 분 (min)이다.
 * 그리고 모든 Rate는 소수 (0.2, 0.4)로 한다.
 */

val unknownTransportation = Transportation("UNKNOWN", R.drawable.ic_taxi, R.string.Unknown, Color(0xFFBBBBBB), listOf())
val taxi = Transportation("TAXI", R.drawable.ic_taxi, R.string.Taxi, Color(0xFFFFDE4D), TaxiData.fareCalcTypes)
val bus = Transportation("BUS", R.drawable.ic_taxi, R.string.Taxi, Color(0xFFFFDE4D), listOf())
val subway = Transportation("SUBWAY", R.drawable.ic_taxi, R.string.Taxi, Color(0xFFFFDE4D), listOf())
val tram = Transportation("TRAM", R.drawable.ic_taxi, R.string.Taxi, Color(0xFFFFDE4D), listOf())
val KTX = Transportation("KTX", R.drawable.ic_taxi, R.string.Taxi, Color(0xFFFFDE4D), listOf())
val SRT = Transportation("SRT", R.drawable.ic_taxi, R.string.Taxi, Color(0xFFFFDE4D), listOf())
val transportations = listOf(
    taxi,
    bus,
    subway,
    tram,
    KTX,
    SRT
)

data class Transportation(
    val id: String, // TAXI
    val icon: Int, // R.drawable.ic_taxi
    val label: Int, // R.string.Taxi
    val color: Color,
    val calcTypes: List<String> // 서울특별시, 경기도, 일반실, 우등실
)

data class TaxiTransportation(
    val minFare: Int, // 기본 요금
    val minDistance: Int, // 기본 요금 거리
    val runFare: Double, // 거리 요금
    val runDistance: Int, // 거리 요금 거리
    val timeFare: Int, // 시간 요금
    val timeTime: Int, // 시간 요금 시간
    val intercityRate: Double, // 시외 할증
    val nightRate: List<Double>, // 심야 할증
    val nightTime: List<List<Int>>, // 심야 할증 시간
)

data class TrainTransportation(
    val minFare: Int, // 기본 요금
    val minDistance: Int, // 기본 요금 거리
    val runFare: Double, // 거리 요금
    val runDistance: Int, // 거리 요금 거리
)