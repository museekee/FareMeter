package kr.musekee.faremeter.libs

import android.content.ContentValues

class RecordDao(private val dbHelper: DatabaseHelper) {
    fun saveData(data: RecordData): Long {
        val db = dbHelper.writableDatabase
        val cv = ContentValues().apply {
            put("ID", data.id)
            put("FARE_CALC_TYPE", data.fareCalcType)
            put("TRANSPORTATION", data.transportation)
            put("END_TIME", data.endTime)
            put("FARE", data.fare)
            put("AVERAGE_SPEED", data.averageSpeed)
            put("TOP_SPEED", data.topSpeed)
            put("DISTANCE", data.distance)
        }

        return db.insert("Records", null, cv)
    }

    fun deleteData(id: String) {
        val db = dbHelper.readableDatabase
        db.delete("Records", "ID = ?", listOf(id).toTypedArray())
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
                            id = it.getString(it.getColumnIndexOrThrow("ID")),
                            fareCalcType = it.getString(it.getColumnIndexOrThrow("FARE_CALC_TYPE")),
                            transportation = it.getString(it.getColumnIndexOrThrow("TRANSPORTATION")),
                            endTime = it.getLong(it.getColumnIndexOrThrow("END_TIME")),
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