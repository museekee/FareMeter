package kr.musekee.faremeter.datas

object TaxiData {
    val seoul = TaxiTransportation(
        minFare = 4800,
        minDistance = 1600,
        runFare = 100,
        runDistance = 131,
        timeFare = 100,
        timeTime = 30,
        intercityRate = 0.2,
        nightRate = listOf(0.2, 0.4, 0.2),
        nightTime = listOf(listOf(22), listOf(23, 0, 1), listOf(2, 3))
    )
    val gyeonggi = TaxiTransportation(
        minFare = 4800,
        minDistance = 1600,
        runFare = 100,
        runDistance = 131,
        timeFare = 100,
        timeTime = 30,
        intercityRate = 0.2,
        nightRate = listOf(0.3),
        nightTime = listOf(listOf(23, 0, 1, 2, 3))
    )
    fun getDataByName(name: String): StandardTransportation {
        return if (name == "seoul")
            seoul
        else if (name == "gyeonggi")
            gyeonggi
        else gyeonggi
    }
}