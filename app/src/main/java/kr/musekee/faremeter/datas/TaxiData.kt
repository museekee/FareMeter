package kr.musekee.faremeter.datas

object TaxiData {
    //region 서울
    val seoul = TaxiTransportation(
        minFare = 4800,
        minDistance = 1600,
        runFare = 100.0,
        runDistance = 131,
        timeFare = 100,
        timeTime = 30,
        intercityRate = 0.2,
        nightRate = listOf(0.2, 0.4, 0.2),
        nightTime = listOf(listOf(22), listOf(23, 0, 1), listOf(2, 3))
    )
    //endregion
    //region 부산
    val busan = TaxiTransportation(
        minFare = 4800,
        minDistance = 2000,
        runFare = 100.0,
        runDistance = 132,
        timeFare = 100,
        timeTime = 33,
        intercityRate = 0.3,
        nightRate = listOf(0.2, 0.3, 0.2),
        nightTime = listOf(listOf(23), listOf(0, 1), listOf(2, 3))
    )
    //endregion
    //region 대구
    val daegu = TaxiTransportation(
        minFare = 4000,
        minDistance = 2000,
        runFare = 100.0,
        runDistance = 130,
        timeFare = 100,
        timeTime = 31,
        intercityRate = 0.3,
        nightRate = listOf(0.2),
        nightTime = listOf(listOf(23, 0, 1, 2, 3))
    )
    //endregion
    //region 인천
    val incheon = TaxiTransportation(
        minFare = 4800,
        minDistance = 1600,
        runFare = 100.0,
        runDistance = 135,
        timeFare = 100,
        timeTime = 33,
        intercityRate = 0.3,
        nightRate = listOf(0.2, 0.4, 0.2),
        nightTime = listOf(listOf(22), listOf(23, 0, 1), listOf(2, 3))
    )
    //endregion
    //region 광주
    val gwangju = TaxiTransportation(
        minFare = 4300,
        minDistance = 2000,
        runFare = 100.0,
        runDistance = 134,
        timeFare = 100,
        timeTime = 32,
        intercityRate = 0.35,
        nightRate = listOf(0.2),
        nightTime = listOf(listOf(0, 1, 2, 3))
    )
    //endregion
    //region 대전
    val daejeon = TaxiTransportation(
        minFare = 4300,
        minDistance = 1800,
        runFare = 100.0,
        runDistance = 132,
        timeFare = 100,
        timeTime = 33,
        intercityRate = 0.3,
        nightRate = listOf(0.2),
        nightTime = listOf(listOf(23, 0, 1, 2, 3))
    )
    //endregion
    //region 울산
    val ulsan = TaxiTransportation(
        minFare = 4000,
        minDistance = 2000,
        runFare = 100.0,
        runDistance = 125,
        timeFare = 100,
        timeTime = 30,
        intercityRate = 0.3,
        nightRate = listOf(0.2),
        nightTime = listOf(listOf(22, 23, 0, 1, 2, 3))
    )
    //endregion
    //region 경기
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
    //endregion
    val fareCalcTypes = listOf("서울특별시", "부산광역시", "대구광역시", "인천광역시", "광주광역시", "대전광역시", "울산광역시", "경기도")
    fun getData(name: String?): TaxiTransportation {
        return when (name) {
            "서울특별시" -> seoul
            "부산광역시" -> busan
            "대구광역시" -> daegu
            "인천광역시" -> incheon
            "광주광역시" -> gwangju
            "대전광역시" -> daejeon
            "울산광역시" -> ulsan
            "경기도" -> gyeonggi
            else -> seoul
        }
    }
}