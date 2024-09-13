package kr.musekee.faremeter.libs

import android.content.ContentValues

class RecordDao(private val dbHelper: DatabaseHelper) {
    fun saveData(data: RecordData): Long {
        val db = dbHelper.writableDatabase
        val cv = ContentValues().apply {
            put("FARE_CALC_TYPE", data.fareCalcType)
            put("TRANSPORTATION", data.transportation)
            put("END_TIME", data.timePos.last().time)
            put("TIMES", data.timePos.map { it.time }.joinToString(","))
            put("FARE", data.fare)
            put("AVERAGE_SPEED", data.averageSpeed)
            put("TOP_SPEED", data.topSpeed)
            put("DISTANCE", data.distance)
            put("LATITUDES", data.timePos.map { it.latitude }.joinToString(","))
            put("LONGITUDES", data.timePos.map { it.longitude }.joinToString(","))
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
                        val timePos = mutableListOf<TimePosition>()
                        val latitudes = it.getString(it.getColumnIndexOrThrow("LATITUDES"))
                        val longitudes = it.getString(it.getColumnIndexOrThrow("LONGITUDES"))
                        val times = it.getString(it.getColumnIndexOrThrow("TIMES"))
                        if ((latitudes == "" || longitudes == "" || times == "").not())
                            times.split(",").mapIndexed { idx, time ->
                                timePos.add(TimePosition(
                                    latitude = latitudes.split(",")[idx].toDouble(),
                                    longitude = longitudes.split(",")[idx].toDouble(),
                                    time = time.toLong()
                                ))
                            }
                        data += RecordData(
                            _id = it.getInt(it.getColumnIndexOrThrow("_ID")),
                            fareCalcType = it.getString(it.getColumnIndexOrThrow("FARE_CALC_TYPE")),
                            transportation = it.getString(it.getColumnIndexOrThrow("TRANSPORTATION")),
                            timePos = timePos,
                            fare = it.getInt(it.getColumnIndexOrThrow("FARE")),
                            averageSpeed = it.getFloat(it.getColumnIndexOrThrow("AVERAGE_SPEED")),
                            topSpeed = it.getFloat(it.getColumnIndexOrThrow("TOP_SPEED")),
                            distance = it.getDouble(it.getColumnIndexOrThrow("DISTANCE"))
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