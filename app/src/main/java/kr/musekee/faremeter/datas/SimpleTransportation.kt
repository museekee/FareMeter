package kr.musekee.faremeter.datas

fun TaxiTransportation(
    minFare: Int,
    minDistance: Int,
    runFare: Int,
    runDistance: Int,
    timeFare: Int,
    timeTime: Int,
    intercityRate: Double,
    nightRate: List<Double>,
    nightTime: List<List<Int>>,
): StandardTransportation {
    return StandardTransportation(
        minFare = minFare,
        minDistance = minDistance,
        runFare = runFare,
        runDistance = runDistance,
        timeFare = timeFare,
        timeTime = timeTime,
        intercityRate = intercityRate,
        nightRate = nightRate,
        nightTime = nightTime,
        morningFare = minFare,
        sectionFare = listOf(),
        sectionDistance = listOf()
    )
}