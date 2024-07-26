package kr.musekee.faremeter.datas

object TaxiData {
    val seoul = TaxiTransportation(
        minFare = 4800,
        minDistance = 50,
        runFare = 100.0,
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
        runFare = 100.0,
        runDistance = 131,
        timeFare = 100,
        timeTime = 30,
        intercityRate = 0.2,
        nightRate = listOf(0.3),
        nightTime = listOf(listOf(23, 0, 1, 2, 3))
    )
    val names = listOf("서울특별시", "경기도")
    fun getDataByName(name: String?): TaxiTransportation {
        return when (name) {
            "서울특별시" -> seoul
            "경기도" -> gyeonggi
            else -> seoul
        }
    }
}