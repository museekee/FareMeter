package kr.musekee.faremeter.libs

import android.content.ContentValues
import kr.musekee.faremeter.libs.DatabaseHelper.Companion.dateFormat
import java.util.Date

class RecordDao(private val dbHelper: DatabaseHelper) {
    fun saveData(data: RecordData): Long {
        val db = dbHelper.writableDatabase
        val cv = ContentValues().apply {
            put("FARE_CALC_TYPE", data.fareCalcType)
            put("TRANSPORTATION", data.transportation)
            put("START_TIME", dateFormat.format(data.startTime))
            put("END_TIME", dateFormat.format(data.endTime))
            put("FARE", data.fare)
            put("AVERAGE_SPEED", data.averageSpeed)
            put("TOP_SPEED", data.topSpeed)
            put("DISTANCE", data.distance)
            put("LATITUDES", data.latitudes.joinToString(","))
            put("LONGITUDES", data.longitudes.joinToString(","))
            put("NO_GPS_TIMES", data.noGPSTimes.joinToString(",") { it.toList().joinToString("/") }) // 100/105,200/210
            put("NO_GPS_LATITUDES", data.noGPSLatitudes.joinToString(",") { it.toList().joinToString("/") })
            put("NO_GPS_LONGITUDES", data.noGPSLongitudes.joinToString(",") { it.toList().joinToString("/") })
        }

        return db.insert("Records", null, cv)
    }

    fun deleteData(id: Int) {
        val db = dbHelper.readableDatabase
        db.delete("Records", "_ID = $id", null)
        db.close()
    }

    inner class DataQueryBuilder {
        private var selection: String? = null
        private var selectionArgs: Array<String>? = null
        private var orderBy: String? = null
        private var limit: String? = null

        fun transportation(transportation: String?): DataQueryBuilder {
            if (transportation != null) {
                selection = "TRANSPORTATION = ?"
                selectionArgs = arrayOf(transportation)
            }
            return this
        }

        fun orderBy(column: String, direction: String = "ASC"): DataQueryBuilder {
            orderBy = "$column $direction"
            return this
        }

        fun limit(limit: Int): DataQueryBuilder {
            this.limit = limit.toString()
            if (limit == 0) this.limit = null
            return this
        }

        fun execute(): List<RecordData> {
            val db = dbHelper.readableDatabase
            val cursor = db.query(
                "Records",
                null, // columns (null means all columns)
                selection,
                selectionArgs,
                null, // groupBy
                null, // having
                orderBy,
                limit
            )

            val data = mutableListOf<RecordData>()
            cursor.use {
                if (it.moveToFirst())
                    do {
                        data += RecordData(
                            _id = it.getInt(it.getColumnIndexOrThrow("_ID")),
                            fareCalcType = it.getString(it.getColumnIndexOrThrow("FARE_CALC_TYPE")),
                            transportation = it.getString(it.getColumnIndexOrThrow("TRANSPORTATION")),
                            startTime = dateFormat.parse(it.getString(it.getColumnIndexOrThrow("START_TIME")))
                                ?: Date(System.currentTimeMillis()),
                            endTime = dateFormat.parse(it.getString(it.getColumnIndexOrThrow("END_TIME")))
                                ?: Date(System.currentTimeMillis()),
                            fare = it.getInt(it.getColumnIndexOrThrow("FARE")),
                            averageSpeed = it.getFloat(it.getColumnIndexOrThrow("AVERAGE_SPEED")),
                            topSpeed = it.getFloat(it.getColumnIndexOrThrow("TOP_SPEED")),
                            distance = it.getDouble(it.getColumnIndexOrThrow("DISTANCE")),
                            latitudes = if (it.getString(it.getColumnIndexOrThrow("LATITUDES")) == "")
                                listOf(0.0)
                            else
                                it.getString(it.getColumnIndexOrThrow("LATITUDES")).split(",").map { v -> v.toDouble() },
                            longitudes = if (it.getString(it.getColumnIndexOrThrow("LONGITUDES")) == "")
                                listOf(0.0)
                            else
                                it.getString(it.getColumnIndexOrThrow("LONGITUDES")).split(",").map { v -> v.toDouble() },
                            noGPSTimes = if (it.getString(it.getColumnIndexOrThrow("NO_GPS_TIMES")) == "")
                                listOf()
                            else
                                it.getString(it.getColumnIndexOrThrow("NO_GPS_TIMES")).split(",").map { v ->
                                val vList = v.split("/").map {
                                    w -> w.toLong()
                                }
                                Pair(vList[0], vList[1])
                            },
                            noGPSLatitudes = if (it.getString(it.getColumnIndexOrThrow("NO_GPS_LATITUDES")) == "")
                                listOf()
                            else
                                it.getString(it.getColumnIndexOrThrow("NO_GPS_LATITUDES")).split(",").map { v ->
                                    val vList = v.split("/").map {
                                            w -> w.toDouble()
                                    }
                                    Pair(vList[0], vList[1])
                                },
                            noGPSLongitudes = if (it.getString(it.getColumnIndexOrThrow("NO_GPS_LONGITUDES")) == "")
                                listOf()
                            else
                                it.getString(it.getColumnIndexOrThrow("NO_GPS_LONGITUDES")).split(",").map { v ->
                                    val vList = v.split("/").map {
                                            w -> w.toDouble()
                                    }
                                    Pair(vList[0], vList[1])
                                }
                        )
                    } while (it.moveToNext())
                it.close()
            }
            return data
        }
    }
    fun getAllData(): DataQueryBuilder {
        return DataQueryBuilder()
    }
}