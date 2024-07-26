package kr.musekee.faremeter.datas

/**
 * 모든 요금의 단위는 원 (￦)이고,
 * 거리의 단위는 미터 (m)이며,
 * 시간의 단위는 분 (min)이다.
 * 그리고 모든 Rate는 소수 (0.2, 0.4)로 한다.
 */
data class StandardTransportation(
    val minFare: Int, // 기본 요금
    val minDistance: Int, // 기본 요금 거리
    val runFare: Double, // 거리 요금
    val runDistance: Int, // 거리 요금 거리
    val timeFare: Int, // 시간 요금
    val timeTime: Int, // 시간 요금 시간
    val intercityRate: Double, // 시외 할증
    val nightRate: List<Double>, // 심야 할증
    val nightTime: List<List<Int>>, // 심야 할증 시간
    val morningFare: Int, // 조조할인된 가격
    val sectionFare: List<Int>, // 구간 요금
    val sectionDistance: List<Int>, // 구간 거리
)
