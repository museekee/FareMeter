package kr.musekee.faremeter.datas

object KTXData {
    val economy = TrainTransportation(
        minFare = 8400,
        minDistance = 50,
        runFare = 164.41,
        runDistance = 1000
    )
    val first = TrainTransportation(
        minFare = 8400,
        minDistance = 50,
        runFare = 230.17,
        runDistance = 1000
    )
    val second = TrainTransportation(
        minFare = 8400,
        minDistance = 60,
        runFare = 168.24,
        runDistance = 1000
    )
    val fareCalcTypes = listOf("일반실", "특실", "우등실")
    fun getData(name: String?): TrainTransportation {
        return when (name) {
            "일반실" -> economy
            "특실" -> first
            "우등실" -> second
            else -> economy
        }
    }
}